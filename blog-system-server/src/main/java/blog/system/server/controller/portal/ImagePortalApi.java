package blog.system.server.controller.portal;


import blog.system.server.response.ResponseResult;
import blog.system.server.services.IImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/portal/image")
public class ImagePortalApi {

    @Autowired
    private IImageService imageService;

    @GetMapping("/{imageId}")
    public void getImage(HttpServletResponse response, @PathVariable("imageId") String imageId) {
        try {
            imageService.viewImage(response, imageId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/qr-code/{code}")
    public void getQrCodeImage(@PathVariable("code") String code, HttpServletResponse response, HttpServletRequest request) {
        imageService.createQrCode(code, response, request);
    }

    @GetMapping("/list/{page}/{size}")
    public ResponseResult listImages(@PathVariable("page") int page,
                                     @PathVariable("size") int size,
                                     @RequestParam(value = "original", required = false) String original) {
        return imageService.listImages(page, size, original);
    }

    @PostMapping("/{original}")
    public ResponseResult uploadImage(@PathVariable("original") String original, @RequestParam("file") MultipartFile file) {
        return imageService.uploadImage(original, file);
    }
}
