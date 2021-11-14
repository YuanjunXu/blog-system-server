package blog.system.server.services.impl;


import blog.system.server.dao.SettingDao;
import blog.system.server.pojo.Setting;
import blog.system.server.response.ResponseResult;
import blog.system.server.services.IWebSizeInfoService;
import blog.system.server.utils.Constants;
import blog.system.server.utils.IdWorker;
import blog.system.server.utils.RedisUtils;
import blog.system.server.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class WebSizeInfoImpl extends BaseService implements IWebSizeInfoService {

    @Autowired
    private SettingDao settingDao;

    @Autowired
    private IdWorker idWorker;

    @Override
    public ResponseResult getWebSizeTitle() {
        Setting title = settingDao.findOneByKey(Constants.Settings.WEB_SIZE_TITLE);
        return ResponseResult.SUCCESS("获取网站title成功.").setData(title);
    }

    @Override
    public ResponseResult putWebSizeTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            return ResponseResult.FAILED("网站标题不可以为空.");
        }
        Setting titleFromDb = settingDao.findOneByKey(Constants.Settings.WEB_SIZE_TITLE);
        if (titleFromDb == null) {
            titleFromDb = new Setting();
            titleFromDb.setId(idWorker.nextId() + "");
            titleFromDb.setUpdateTime(new Date());
            titleFromDb.setCreateTime(new Date());
            titleFromDb.setKey(Constants.Settings.WEB_SIZE_TITLE);
        }
        titleFromDb.setValue(title);
        settingDao.save(titleFromDb);
        return ResponseResult.SUCCESS("网站Title更新成功.");
    }

    @Override
    public ResponseResult getSeoInfo() {
        Setting description = settingDao.findOneByKey(Constants.Settings.WEB_SIZE_DESCRIPTION);
        Setting keyWords = settingDao.findOneByKey(Constants.Settings.WEB_SIZE_KEYWORDS);
        Map<String, String> result = new HashMap<>();
        result.put(description.getKey(), description.getValue());
        result.put(keyWords.getKey(), keyWords.getValue());
        return ResponseResult.SUCCESS("获取SEO信息成功.").setData(result);
    }

    @Override
    public ResponseResult putSeoInfo(String keywords, String description) {
        //判断
        if (TextUtils.isEmpty(keywords)) {
            return ResponseResult.FAILED("关键字不可以为空.");
        }
        if (TextUtils.isEmpty(description)) {
            return ResponseResult.FAILED("网站描述不可以为空.");
        }
        Setting descriptionFromDb = settingDao.findOneByKey(Constants.Settings.WEB_SIZE_DESCRIPTION);
        if (descriptionFromDb == null) {
            descriptionFromDb = new Setting();
            descriptionFromDb.setId(idWorker.nextId() + "");
            descriptionFromDb.setCreateTime(new Date());
            descriptionFromDb.setUpdateTime(new Date());
            descriptionFromDb.setKey(Constants.Settings.WEB_SIZE_DESCRIPTION);
        }
        descriptionFromDb.setValue(description);
        settingDao.save(descriptionFromDb);
        Setting keyWordsFromDb = settingDao.findOneByKey(Constants.Settings.WEB_SIZE_KEYWORDS);
        if (keyWordsFromDb == null) {
            keyWordsFromDb = new Setting();
            keyWordsFromDb.setId(idWorker.nextId() + "");
            keyWordsFromDb.setCreateTime(new Date());
            keyWordsFromDb.setUpdateTime(new Date());
            keyWordsFromDb.setKey(Constants.Settings.WEB_SIZE_KEYWORDS);
        }
        keyWordsFromDb.setValue(keywords);
        settingDao.save(keyWordsFromDb);
        return ResponseResult.SUCCESS("更新SEO信息成功.");
    }

    /**
     * 这个是全网站的访问量，要做得细一点，还得分来源
     * 这里只统计浏览量，只统计文章的浏览量，提供一个浏览量的统计接口（页面级的）
     *
     * @return 浏览量
     */
    @Override
    public ResponseResult getSizeViewCount() {
        //先从redis里拿出来
        String viewCountStr = (String) redisUtils.get(Constants.Settings.WEB_SIZE_VIEW_COUNT);
        Setting viewCount = settingDao.findOneByKey(Constants.Settings.WEB_SIZE_VIEW_COUNT);
        if (viewCount == null) {
            viewCount = this.initViewItem();
            settingDao.save(viewCount);
        }
        if (TextUtils.isEmpty(viewCountStr)) {
            viewCountStr = viewCount.getValue();
            redisUtils.set(Constants.Settings.WEB_SIZE_VIEW_COUNT, viewCountStr);
        } else {
            //把redis里的更新到数据里
            viewCount.setValue(viewCountStr);
            settingDao.save(viewCount);
        }
        Map<String, Integer> result = new HashMap<>();
        result.put(viewCount.getKey(), Integer.valueOf(viewCount.getValue()));
        return ResponseResult.SUCCESS("获取网站浏览量成功.").setData(result);
    }

    private Setting initViewItem() {
        Setting viewCount = new Setting();
        viewCount.setId(idWorker.nextId() + "");
        viewCount.setKey(Constants.Settings.WEB_SIZE_VIEW_COUNT);
        viewCount.setUpdateTime(new Date());
        viewCount.setCreateTime(new Date());
        viewCount.setValue("1");
        return viewCount;
    }

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 1、并发量
     * 2、过滤相通的IP/ID
     * 3、防止攻击，比如太频繁的访问，就提示请稍后重试.
     */
    @Override
    public void updateViewCount() {
        //redis的更新时机：
        Object viewCount = redisUtils.get(Constants.Settings.WEB_SIZE_VIEW_COUNT);
        if (viewCount == null) {
            Setting setting = settingDao.findOneByKey(Constants.Settings.WEB_SIZE_VIEW_COUNT);
            if (setting == null) {
                setting = this.initViewItem();
                settingDao.save(setting);
            }
            redisUtils.set(Constants.Settings.WEB_SIZE_VIEW_COUNT, setting.getValue());
        } else {
            //自增
            redisUtils.incr(Constants.Settings.WEB_SIZE_VIEW_COUNT, 1);
        }
    }
}
