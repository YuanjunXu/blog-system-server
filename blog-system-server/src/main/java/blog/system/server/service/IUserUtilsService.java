package blog.system.server.service;

import blog.system.server.utils.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


public interface IUserUtilsService {
    ResponseResult<Long> getRegisterCount();

    ResponseResult checkEmailCode(String email, String emailCode, String captchaCode);

    ResponseResult<Map<String, Object>> getPcLoginQrCodeInfo();




    ResponseResult parseToken();


    ResponseResult checkEmail(String email);

    ResponseResult checkUserName(String userName);


    void createCaptcha(HttpServletResponse response) throws Exception;

    ResponseResult sendEmail(String type, HttpServletRequest request, String address, String captchaCode);

}
