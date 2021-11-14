package blog.system.server.services;


import blog.system.server.response.ResponseResult;

public interface IWebSizeInfoService {
    ResponseResult getWebSizeTitle();

    ResponseResult putWebSizeTitle(String title);

    ResponseResult getSeoInfo();

    ResponseResult putSeoInfo(String keywords, String description);

    ResponseResult getSizeViewCount();

    void updateViewCount();
    
}
