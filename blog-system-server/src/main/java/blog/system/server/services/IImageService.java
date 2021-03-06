package blog.system.server.services;

import blog.system.server.response.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface IImageService {
    ResponseResult uploadImage(String original, MultipartFile file);

    void viewImage(HttpServletResponse response, String imageId) throws IOException;

    ResponseResult listImages(int page, int size, String original);

    ResponseResult deleteById(String imageId);

    void createQrCode(String code, HttpServletResponse response, HttpServletRequest request);
}
