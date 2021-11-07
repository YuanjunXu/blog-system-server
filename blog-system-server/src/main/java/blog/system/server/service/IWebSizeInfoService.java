package blog.system.server.service;

import blog.system.server.entity.SettingsEntity;
import blog.system.server.utils.ResponseResult;

import java.util.Map;

public interface IWebSizeInfoService {
    ResponseResult<SettingsEntity> getWebSizeTitle();

    ResponseResult putWebSizeTitle(String title);

    ResponseResult<Map<String, String>> getSeoInfo();

    ResponseResult putSeoInfo(String keywords, String description);

    ResponseResult<Map<String, Integer>>  getSizeViewCount();

    void updateViewCount();
    
}
