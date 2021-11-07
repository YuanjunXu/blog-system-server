package blog.system.server.controller.portal;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 应用下载
 * 应用检查更新
 */
@Api(tags = "应用下载 应用检查更新")
@RestController
@RequestMapping("/portal/app")
public class AppApi {

    /**
     * 给第三方扫描下载APP用的接口
     *
     * @return
     */
    //http://localhost:2020/portal/app/===728989028669456384
    @ApiOperation("给第三方扫描下载APP用的接口")
    @GetMapping("/{code}")
    public void downloadAppForThirdPartScan(@PathVariable("code") String code,
                                            HttpServletRequest servletRequest,
                                            HttpServletResponse response) {
        //TODO:直接把最新的APP写出去
    }
}
