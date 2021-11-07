package blog.system.server.service;

import blog.system.server.utils.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public interface IImageService {
    ResponseResult<Map<String, String>> uploadImage(String original, MultipartFile file);

    void viewImage(HttpServletResponse response, String imageId) throws IOException;

    ResponseResult listImages(int page, int size, String original);

    ResponseResult deleteById(String imageId);

    void createQrCode(String code, HttpServletResponse response, HttpServletRequest request);
}
