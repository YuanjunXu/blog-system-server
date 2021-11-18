package blog.system.server.services.impl;

import blog.system.server.dao.RefreshTokenDao;
import blog.system.server.dao.UserDao;
import blog.system.server.pojo.RefreshToken;
import blog.system.server.pojo.SobUser;
import blog.system.server.services.IBaseUserService;
import blog.system.server.utils.*;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@Slf4j
public class BaseUserService extends BaseService implements IBaseUserService {


    @Autowired
    protected CountDownLatchManager countDownLatchManager;

    @Autowired
    protected BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    protected RefreshTokenDao refreshTokenDao;

    @Autowired
    protected Gson gson;


    @Autowired
    protected UserDao userDao;


    @Autowired
    protected RedisUtils redisUtils;


    @Autowired
    protected IdWorker idWorker;


    /**
     * 本质，通过携带的token_key检查用户是否有登录，如果登录了，就返回用户信息
     *
     * @return
     */
    @Override
    public SobUser checkSobUser() {
        //拿到token_key
        String tokenKey = CookieUtils.getCookie(getRequest(), Constants.User.COOKIE_TOKE_KEY);
        log.info("checkSobUser tokenKey == > " + tokenKey);
        if (TextUtils.isEmpty(tokenKey)) {
            return null;
        }
        SobUser sobUser = parseByTokenKey(tokenKey);
        //token中要解析此请求是什么端的
        String from = tokenKey.startsWith(Constants.FROM_PC) ? Constants.FROM_PC : Constants.FROM_MOTILE;
        if (sobUser == null) {
            //说明解析出错了或者过期了
            //1、去mysql查询refreshToken
            //如果是从pc，我们就以pc的token_key来查
            //如果mobile的就以mobile_key来查
            RefreshToken refreshToken;
            if (Constants.FROM_PC.equals(from)) {
                refreshToken = refreshTokenDao.findOneByTokenKey(tokenKey);
            } else {
                refreshToken = refreshTokenDao.findOneByMobileTokenKey(tokenKey);
            }
            //2、如果不存在，就是当前访问没有登录，提示用户登录
            if (refreshToken == null) {
                log.info("refresh token is null...");
                return null;
            }
            //3、如果存在，就解析refreshToken
            try {
                //这个解析有可以有出错，就过期了
                JwtUtil.parseJWT(refreshToken.getRefreshToken());
                //5、如果refreshToken有效，创建新的token，和新的refreshToken
                String userId = refreshToken.getUserId();
                SobUser userFromDb = userDao.findOneById(userId);
                //千万别这么干，事务还没有提交，如果这样设置，数据库里的密码就没有了
                //userFromDb.setPassword("");
                //删掉refreshToken的记录
                String newTokenKey = createToken(getResponse(), userFromDb, from);
                //返回token
                log.info("created new token and refresh token...");
                return parseByTokenKey(newTokenKey);
            } catch (Exception e1) {
                log.info("refresh token is 过期了...");
                //4、如果refreshToken过期了，就当前访问没有登录，提示用户登录
                return null;
            }
        }
        return sobUser;
    }

    /**
     * @param response
     * @param userFromDb
     * @param from
     * @return token_key
     */
    protected String createToken(HttpServletResponse response, SobUser userFromDb, String from) {
        String oldTokenKey = CookieUtils.getCookie(getRequest(), Constants.User.COOKIE_TOKE_KEY);
        //不能干掉了
        RefreshToken oldRefreshToken = refreshTokenDao.findOneByUserId(userFromDb.getId());
        if (Constants.FROM_MOTILE.equals(from)) {
            if (oldRefreshToken != null) {
                redisUtils.del(Constants.User.KEY_TOKEN + oldRefreshToken.getMobileTokenKey());
            }
            //根据来源删除refreshToken中对应的token_key
            refreshTokenDao.deleteMobileTokenKey(oldTokenKey);
        } else if (Constants.FROM_PC.equals(from)) {
            //确保单端登录，删除redis里的token。注释下面代码允许多端同时登陆
//            if (oldRefreshToken != null) {
//                redisUtils.del(Constants.User.KEY_TOKEN + oldRefreshToken.getTokenKey());
//            }
//            refreshTokenDao.deletePcTokenKey(oldTokenKey);
        }

        //生成token,claims已经包含from了
        Map<String, Object> claims = ClaimsUtils.sobUser2Claims(userFromDb, from);
        //token默认有效为2个小时
        String token = JwtUtil.createToken(claims);
        //返回token的md5值，token会保存到redis里
        //前端访问的时候，携带token的md5key，从redis中获取即可
        String tokenKey = from + DigestUtils.md5DigestAsHex(token.getBytes());
        //保存token到redis里，有效期为2个小时，key是tokenKey
        redisUtils.set(Constants.User.KEY_TOKEN + tokenKey, token, Constants.TimeValueInSecond.HOUR_2);
        //把tokenKey写到cookies里
        //这个要动态获取，可以从request里获取，
        CookieUtils.setUpCookie(response, Constants.User.COOKIE_TOKE_KEY, tokenKey);
        //先判断数据库里没有有没refreshToken
        //如果有的话就更新
        //如果没有就新创建
        RefreshToken refreshToken = refreshTokenDao.findOneByUserId(userFromDb.getId());
        if (refreshToken == null) {
            refreshToken = new RefreshToken();
            refreshToken.setId(idWorker.nextId() + "");
            refreshToken.setCreateTime(new Date());
            refreshToken.setUserId(userFromDb.getId());
        }
        //不管过期了，还是新登录，都生成/更新refreshToken
        //生成refreshToken
        String refreshTokenValue = JwtUtil.createRefreshToken(userFromDb.getId(), Constants.TimeValueInMillions.MONTH);
        //保存到数据库里
        //refreshToken，tokenKey，用户ID，创建时间，更新时间
        refreshToken.setRefreshToken(refreshTokenValue);
        //要判断来源，如果是移动端的就设置到移动端那里去
        //如果是PC的就设置到默认的
        if (Constants.FROM_PC.equals(from)) {
            refreshToken.setTokenKey(tokenKey);
        } else {
            refreshToken.setMobileTokenKey(tokenKey);
        }
        refreshToken.setUpdateTime(new Date());
        refreshTokenDao.save(refreshToken);
        return tokenKey;
    }


    /**
     * 解析此token是从PC端来的还是移动端来的，使用要判空
     *
     * @param tokeKey
     * @return
     */
    protected String parseFrom(String tokeKey) {
        String token = (String) redisUtils.get(Constants.User.KEY_TOKEN + tokeKey);
        if (token != null) {
            try {
                Claims claims = JwtUtil.parseJWT(token);
                return ClaimsUtils.getFrom(claims);
            } catch (Exception e) {
                log.info("parseByTokenKey == > " + tokeKey + " 过期了...");
            }
        }
        return null;
    }

    protected SobUser parseByTokenKey(String tokeKey) {
        String token = (String) redisUtils.get(Constants.User.KEY_TOKEN + tokeKey);
        log.info("parseByTokenKey token == > " + token);
        if (token != null) {
            try {
                Claims claims = JwtUtil.parseJWT(token);
                return ClaimsUtils.claims2SobUser(claims);
            } catch (Exception e) {
                log.info("parseByTokenKey == > " + tokeKey + " 过期了...");
                return null;
            }
        }
        return null;
    }

}
