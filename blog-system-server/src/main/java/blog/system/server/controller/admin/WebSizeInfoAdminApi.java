package blog.system.server.controller.admin;

import blog.system.server.interceptor.CheckTooFrequentCommit;
import blog.system.server.service.IWebSizeInfoService;
import blog.system.server.utils.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "网站信息")
@RestController
@RequestMapping("/admin/web_size_info")
public class WebSizeInfoAdminApi {

    @Autowired
    private IWebSizeInfoService iWebSizeInfoService;

    @ApiOperation("获取网站信息")
    @PreAuthorize("@permission.admin()")
    @GetMapping("/title")
    public ResponseResult getWebSizeTitle() {
        return iWebSizeInfoService.getWebSizeTitle();
    }

    @ApiOperation("获更新网站信息")
    @CheckTooFrequentCommit
    @PreAuthorize("@permission.admin()")
    @PutMapping("/title")
    public ResponseResult upWebSizeTitle(@RequestParam("title") String title) {
        return iWebSizeInfoService.putWebSizeTitle(title);
    }

    @ApiOperation("获取网站SEO信息")
    @PreAuthorize("@permission.admin()")
    @GetMapping("/seo")
    public ResponseResult getSeoInfo() {
        return iWebSizeInfoService.getSeoInfo();
    }

    @ApiOperation("添加网站SEO信息")
    @CheckTooFrequentCommit
    @PreAuthorize("@permission.admin()")
    @PutMapping("/seo")
    public ResponseResult putSeoInfo(@RequestParam("keywords") String keywords,
                                     @RequestParam("description") String description) {
        return iWebSizeInfoService.putSeoInfo(keywords, description);
    }
    @ApiOperation("获取访问量")
    @PreAuthorize("@permission.admin()")
    @GetMapping("/view_count")
    public ResponseResult getWebSizeViewCount() {
        return iWebSizeInfoService.getSizeViewCount();
    }




}
