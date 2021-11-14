package blog.system.server.services.impl;

import blog.system.server.dao.UserNoPasswordDao;
import blog.system.server.pojo.SobUser;
import blog.system.server.pojo.SobUserNoPassword;
import blog.system.server.response.ResponseResult;
import blog.system.server.response.ResponseState;
import blog.system.server.services.IUserService;
import blog.system.server.utils.Constants;
import blog.system.server.utils.CookieUtils;
import blog.system.server.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional
public class UserServiceImpl extends BaseUserService implements IUserService {

    @Override
    public ResponseResult register(SobUser sobUser, String emailCode, String captchaCode) {
        //第一步：检查当前用户名是否已经注册
        String userName = sobUser.getUserName();
        if (TextUtils.isEmpty(userName)) {
            return ResponseResult.FAILED("用户名不可以为空.");
        }
        SobUser userByName = userDao.findOneByUserName(userName);
        if (userByName != null) {
            return ResponseResult.FAILED("该用户名已注册.");
        }
        //第二步：检查邮箱格式是否正确
        String email = sobUser.getEmail();
        if (TextUtils.isEmpty(email)) {
            return ResponseResult.FAILED("邮箱地址不可以为空.");
        }
        if (!TextUtils.isEmailAddressOk(email)) {
            return ResponseResult.FAILED("邮箱地址格式不正确");
        }
        //第三步：检查该邮箱是否已经注册
        SobUser userByEmail = userDao.findOneByEmail(email);
        if (userByEmail != null) {
            return ResponseResult.FAILED("该邮箱地址已经注册");
        }
        //第四步：检查邮箱验证码是否正确
        String emailVerifyCode = (String) redisUtils.get(Constants.User.KEY_EMAIL_CODE_CONTENT + email);
        if (TextUtils.isEmpty(emailVerifyCode)) {
            return ResponseResult.FAILED("邮箱验证码已过期");
        }
        if (!emailVerifyCode.equals(emailCode)) {
            return ResponseResult.FAILED("邮箱验证码不正确");
        } else {
            //正确，干掉redis里的内容
            redisUtils.del(Constants.User.KEY_EMAIL_CODE_CONTENT + email);
        }
        //从redis里拿
        String captchaKey = CookieUtils.getCookie(getRequest(), Constants.User.LAST_CAPTCHA_ID);
        if (TextUtils.isEmpty(captchaKey)) {
            return ResponseResult.FAILED("请允许保留cookies信息");
        }
        //第五步：检查图灵验证码是否正确
        String captchaVerifyCode = (String) redisUtils.get(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);
        if (TextUtils.isEmpty(captchaVerifyCode)) {
            return ResponseResult.FAILED("人类验证码已过期");
        }
        if (!captchaVerifyCode.equals(captchaCode)) {
            return ResponseResult.FAILED("人类验证码不正确");
        } else {
            redisUtils.del(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);
        }
        //达到可以注册的条件
        //第六步：对密码进行加密
        String password = sobUser.getPassword();
        if (TextUtils.isEmpty(password)) {
            return ResponseResult.FAILED("密码不可以为空");
        }
        sobUser.setPassword(bCryptPasswordEncoder.encode(sobUser.getPassword()));
        //第七步：补全数据
        //包括：注册IP,登录IP,角色,头像,创建时间,更新时间
        String ipAddress = getRequest().getRemoteAddr();
        sobUser.setRegIp(ipAddress);
        sobUser.setLoginIp(ipAddress);
        sobUser.setUpdateTime(new Date());
        sobUser.setCreateTime(new Date());
        sobUser.setAvatar(Constants.User.DEFAULT_AVATAR);
        sobUser.setRoles(Constants.User.ROLE_NORMAL);
        sobUser.setState("1");
        sobUser.setId(idWorker.nextId() + "");
        //第八步：保存到数据库中
        userDao.save(sobUser);
        CookieUtils.deleteCookie(getResponse(), Constants.User.LAST_CAPTCHA_ID);
        //第九步：返回结果
        return ResponseResult.GET(ResponseState.JOIN_IN_SUCCESS);
    }





    /**
     * 删除用户，并不是真的删除
     * 而是修改状态
     * <p>
     * PS：需要管理员权限
     */
    @Override
    public ResponseResult deleteUserById(String userId) {
        //可以删除用户了
        int result = userDao.deleteUserByState(userId);
        if (result > 0) {
            return ResponseResult.SUCCESS("删除成功");
        }
        return ResponseResult.FAILED("用户不存在");
    }

    @Autowired
    private UserNoPasswordDao userNoPasswordDao;

    @Override
    public ResponseResult listUsers(int page, int size, String userName, String email) {
        page = checkPage(page);
        size = checkPage(size);
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<SobUserNoPassword> all = userNoPasswordDao.findAll(new Specification<SobUserNoPassword>() {
            @Override
            public Predicate toPredicate(Root<SobUserNoPassword> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (!TextUtils.isEmpty(userName)) {
                    Predicate preUser = cb.like(root.get("userName").as(String.class), "%" + userName + "%");
                    predicates.add(preUser);
                }

                if (!TextUtils.isEmpty(email)) {
                    Predicate preEmail = cb.equal(root.get("email").as(String.class), email);
                    predicates.add(preEmail);
                }
                Predicate[] preArray = new Predicate[predicates.size()];
                predicates.toArray(preArray);
                return cb.and(preArray);
            }
        }, pageable);
        return ResponseResult.SUCCESS("获取用户列表成功.").setData(all);
    }

    /**
     * 更新密码
     *
     * @param verifyCode
     * @param sobUser
     * @return
     */
    @Override
    public ResponseResult updateUserPassword(String verifyCode, SobUser sobUser) {
        //检查邮箱是否有填写
        String email = sobUser.getEmail();
        if (TextUtils.isEmpty(email)) {
            return ResponseResult.FAILED("邮箱不可以为空.");
        }
        //根据邮箱去redis里拿验证
        //进行对比
        String redisVerifyCode = (String) redisUtils.get(Constants.User.KEY_EMAIL_CODE_CONTENT + email);
        if (redisVerifyCode == null || !redisVerifyCode.equals(verifyCode)) {
            return ResponseResult.FAILED("验证码错误.");
        }
        redisUtils.del(Constants.User.KEY_EMAIL_CODE_CONTENT + email);
        int result = userDao.updatePasswordByEmail(bCryptPasswordEncoder.encode(sobUser.getPassword()), email);
        //修改密码
        return result > 0 ? ResponseResult.SUCCESS("密码修改成功") : ResponseResult.FAILED("密码修改失败");
    }

    /**
     * 更新邮箱地址
     *
     * @param email
     * @param verifyCode
     * @return
     */
    @Override
    public ResponseResult updateEmail(String email, String verifyCode) {
        //1、确保用户已经登录了
        SobUser sobUser = this.checkSobUser();
        //没有登录
        if (sobUser == null) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        //2、对比验证码，确保新的邮箱地址是属于当前用户的
        String redisVerifyCode = (String) redisUtils.get(Constants.User.KEY_EMAIL_CODE_CONTENT + email);
        if (TextUtils.isEmpty(redisVerifyCode) || !redisVerifyCode.equals(verifyCode)) {
            return ResponseResult.FAILED("验证码错误");
        }
        //验证码正确，删除验证码
        redisUtils.del(Constants.User.KEY_EMAIL_CODE_CONTENT + email);
        //可以修改邮箱
        int result = userDao.updateEmailById(email, sobUser.getId());
        return result > 0 ? ResponseResult.SUCCESS("邮箱修改成功") : ResponseResult.FAILED("邮箱修改失败");
    }






    @Override
    public ResponseResult resetPassword(String userId, String password) {
        //查询出用户来
        SobUser user = userDao.findOneById(userId);
        //判断是否存在
        if (user == null) {
            return ResponseResult.FAILED("用户不存在.");
        }
        //密码进行加密
        user.setPassword(bCryptPasswordEncoder.encode(password));
        //处理结果
        userDao.save(user);
        return ResponseResult.SUCCESS("密码重置成功.");
    }


}
