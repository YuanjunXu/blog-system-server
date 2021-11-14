package blog.system.server.controller.admin;

import blog.system.server.interceptor.CheckTooFrequentCommit;
import blog.system.server.response.ResponseResult;
import blog.system.server.services.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/image")
public class ImageAdminApi {

    @Autowired
    private IImageService imageService;

    /**
     * 关于图片（文件）上传
     * 一般来说，现在比较常用的是对象存储--->很简单，看文档就可以学会了
     * 使用 Nginx + fastDFS == > fastDFS -- > 处理文件上传， Nginx -- > 负责处理文件访问
     *
     * @param file
     * @return
     */
    @CheckTooFrequentCommit
    @PreAuthorize("@permission.admin()")
    @PostMapping("/{original}")
    public ResponseResult uploadImage(@PathVariable("original") String original, @RequestParam("file") MultipartFile file) {
        return imageService.uploadImage(original, file);
    }

    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{imageId}")
    public ResponseResult deleteImage(@PathVariable("imageId") String imageId) {
        return imageService.deleteById(imageId);
    }

    @PreAuthorize("@permission.admin()")
    @GetMapping("/list/{page}/{size}")
    public ResponseResult listImages(@PathVariable("page") int page,
                                     @PathVariable("size") int size,
                                     @RequestParam(value = "original", required = false) String original) {
        return imageService.listImages(page, size, original);
    }
}
