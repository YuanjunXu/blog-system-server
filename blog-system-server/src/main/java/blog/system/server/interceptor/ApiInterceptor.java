package blog.system.server.interceptor;

import blog.system.server.utils.*;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 拦截器：拦截用户提交频率
 */
@Component
@Slf4j
public class ApiInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private Gson gson;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            //某一些提交的请求需要拦截
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            CheckTooFrequentCommit methodAnnotation = handlerMethod.getMethodAnnotation(CheckTooFrequentCommit.class);
            if (methodAnnotation != null) {
                String methodName = handlerMethod.getMethod().getName();
                //所有提交内容的方法，必须用户登录的，所以使用token作为key来记录请求频率
                String tokenKey = CookieUtils.getCookie(request, Constants.User.COOKIE_TOKE_KEY);
                log.info("tokenKey -||- > " + tokenKey);
                if (!TextUtils.isEmpty(tokenKey)) {
                    String hasCommit = (String) redisUtils.get(Constants.User.KEY_COMMIT_TOKEN_RECORD + tokenKey + methodName);
                    if (!TextUtils.isEmpty(hasCommit)) {
                        //从redis里获取，判断是否存在，如果存在，则返回提交太频繁
                        response.setCharacterEncoding("UTF-8");
                        response.setContentType("application/json");
                        ResponseResult failed = new ResponseResult(HttpStatus.BAD_REQUEST,"提交过于频繁,请稍后重试.");
                        PrintWriter writer = response.getWriter();
                        writer.write(gson.toJson(failed));
                        writer.flush();
                        return false;
                    } else {
                        //如果不存在，说明可以提交，并且记录此次提交，有效期为30秒
                        redisUtils.set(Constants.User.KEY_COMMIT_TOKEN_RECORD + tokenKey + methodName,
                                "true", Constants.TimeValueInSecond.SECOND_10);
                    }
                }
                //去判断是否真提交太频繁了
                log.info("check commit too frequent...");
            }
        }
        //true表示放行
        //false表示拦截
        return true;
    }
}
