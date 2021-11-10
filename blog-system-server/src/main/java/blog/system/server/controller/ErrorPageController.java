package blog.system.server.controller;

import blog.system.server.utils.ResponseResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 错误码转统一返回的结果
 */
@RestController
public class ErrorPageController {

    @RequestMapping("/404")
    public ResponseResult page404() {
        return new ResponseResult(HttpStatus.NOT_FOUND, "页面丢失！");
    }

    @RequestMapping("/403")
    public ResponseResult page403() {
        return new ResponseResult(HttpStatus.FORBIDDEN, "禁止访问！");
    }

    @RequestMapping("/504")
    public ResponseResult page504() {
        return new ResponseResult(HttpStatus.GATEWAY_TIMEOUT, "系统繁忙，稍后重试！");
    }

    @RequestMapping("/505")
    public ResponseResult page505() {
        return new ResponseResult(HttpStatus.HTTP_VERSION_NOT_SUPPORTED, "请求错误，请检查数据！");
    }
}
