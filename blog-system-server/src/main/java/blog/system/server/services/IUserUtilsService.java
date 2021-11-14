package blog.system.server.services;


import blog.system.server.response.ResponseResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface IUserUtilsService {
    ResponseResult getRegisterCount();

    ResponseResult checkEmailCode(String email, String emailCode, String captchaCode);

    ResponseResult getPcLoginQrCodeInfo();




    ResponseResult parseToken();


    ResponseResult checkEmail(String email);

    ResponseResult checkUserName(String userName);


    void createCaptcha(HttpServletResponse response) throws Exception;

    ResponseResult sendEmail(String type, HttpServletRequest request, String address, String captchaCode);

}
