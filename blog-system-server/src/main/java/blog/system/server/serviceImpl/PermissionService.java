package blog.system.server.serviceImpl;

import blog.system.server.entity.UserEntity;
import blog.system.server.service.IUserService;
import blog.system.server.utils.Constants;
import blog.system.server.utils.CookieUtils;
import blog.system.server.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Service("permission")
public class PermissionService {

    @Autowired
    private IUserService userService;

    /**
     * 判断是不是管理员
     *
     * @return
     */
    public boolean admin() {
        //拿到request和response
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String tokeKey = CookieUtils.getCookie(request, Constants.User.COOKIE_TOKE_KEY);
        //没有令牌的key，没有登录，不用往下执行了
        if (TextUtils.isEmpty(tokeKey)) {
            return false;
        }

        UserEntity sobUser = userService.checkSobUser();
        if (sobUser == null) {
            return false;
        }
        if (Constants.User.ROLE_ADMIN.equals(sobUser.getRoles())) {
            //管理员
            return true;
        }
        return false;
    }

}
