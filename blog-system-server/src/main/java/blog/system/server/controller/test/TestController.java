package blog.system.server.controller.test;


import blog.system.server.dao.CommentDao;
import blog.system.server.dao.LabelDao;
import blog.system.server.pojo.*;
import blog.system.server.response.ResponseResult;
import blog.system.server.response.ResponseState;
import blog.system.server.services.IUserService;
import blog.system.server.services.impl.SolrTestService;
import blog.system.server.utils.Constants;
import blog.system.server.utils.CookieUtils;
import blog.system.server.utils.IdWorker;
import blog.system.server.utils.RedisUtils;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

//
//@Transactional
@Slf4j
@RestController
//@RequestMapping("/test")
public class TestController {


    @Autowired
    private IdWorker idWorker;

    @Autowired
    private LabelDao labelDao;

    @GetMapping("/hello-world")
    public ResponseResult helloWorld() {
        log.info("hello world...");
        String captchaContent = (String) redisUtils.get(Constants.User.KEY_CAPTCHA_CONTENT + "123456");
        log.info("captchaContent == >" + captchaContent);
        return ResponseResult.SUCCESS().setData("hello world");
    }

    @GetMapping("/test-json")
    public ResponseResult testJson() {
        User user = new User("特朗普", 73, "male");
        House house = new House("白宫", "华盛顿特区-宾夕法尼亚大道-1600号");
        user.setHouse(house);
        return ResponseResult.SUCCESS().setData(user);
    }

    @PostMapping("/test-login")
    public ResponseResult testLogin(@RequestBody User user) {
        log.info("user name -== > " + user.getUserName());
        log.info("password  -== > " + user.getPassword());
        ResponseResult loginSuccess = new ResponseResult(ResponseState.LOGIN_SUCCESS);
        return loginSuccess.setData(user);
    }

    @PostMapping("/label")
    public ResponseResult addLabel(@RequestBody Label label) {
        //判断数据是否有效
        //补全数据
        label.setId(idWorker.nextId() + "");
        label.setCreateTime(new Date());
        label.setUpdateTime(new Date());
        //保存数据
        labelDao.save(label);
        return ResponseResult.SUCCESS("测试标签添加成功");
    }

    @DeleteMapping("/label/{labelId}")
    public ResponseResult deleteLabel(@PathVariable("labelId") String labelId) {
        int deleteResult = labelDao.customDeleteLabelById(labelId);
        log.info("deleteResult == > " + deleteResult);
        if (deleteResult > 0) {
            return ResponseResult.SUCCESS("删除标签成功");
        } else {
            return ResponseResult.FAILED("标签不存在");
        }
    }

    @PutMapping("/label/{labelId}")
    public ResponseResult updateLabel(@PathVariable("labelId") String labelId, @RequestBody Label label) {
        Label dbLabel = labelDao.findOneById(labelId);
        if (dbLabel == null) {
            return ResponseResult.FAILED("标签不存在");
        }
        dbLabel.setCount(label.getCount());
        dbLabel.setName(label.getName());
        dbLabel.setUpdateTime(new Date());
        labelDao.save(dbLabel);
        return ResponseResult.SUCCESS("修改成功");
    }

    @GetMapping("/label/{labelId}")
    public ResponseResult getLabelById(@PathVariable("labelId") String labelId) {
        Label dbLabel = labelDao.findOneById(labelId);
        if (dbLabel == null) {
            return ResponseResult.FAILED("标签不存在");
        }
        return ResponseResult.SUCCESS("获取标签成功").setData(dbLabel);
    }

    @GetMapping("/label/list/{page}/{size}")
    public ResponseResult listLabels(@PathVariable("page") int page, @PathVariable("size") int size) {
        if (page < 1) {
            page = 1;
        }
        if (size <= 0) {
            size = Constants.Page.DEFAULT_SIZE;
        }
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Label> result = labelDao.findAll(pageable);
        return ResponseResult.SUCCESS("获取成功").setData(result);
    }

    @GetMapping("/label/search")
    public ResponseResult doLabelSearch(@RequestParam("keyword") String keyword, @RequestParam("count") int count) {
        List<Label> all = labelDao.findAll(new Specification<Label>() {
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Predicate namePre = cb.like(root.get("name").as(String.class), "%" + keyword + "%");
                Predicate countPre = cb.equal(root.get("count").as(Integer.class), count);
                Predicate and = cb.and(namePre, countPre);
                return and;
            }
        });
        if (all.size() == 0) {
            return ResponseResult.FAILED("结果为空");
        }
        return ResponseResult.SUCCESS("查找成功").setData(all);
    }


    @Autowired
    private RedisUtils redisUtils;

    //http://localhost:2020/test/captcha
    @RequestMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 设置请求头为输出图片类型
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        // 三个参数分别为宽、高、位数
        SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 5);
        // 设置字体
        // specCaptcha.setFont(new Font("Verdana", Font.PLAIN, 32));  // 有默认字体，可以不用设置
        specCaptcha.setFont(Captcha.FONT_1);
        // 设置类型，纯数字、纯字母、字母数字混合
        //specCaptcha.setCharType(Captcha.TYPE_ONLY_NUMBER);
        specCaptcha.setCharType(Captcha.TYPE_DEFAULT);

        String content = specCaptcha.text().toLowerCase();
        log.info("captcha content == > " + content);
        // 验证码存入session
        //request.getSession().setAttribute("captcha", content);
        // 保存到redis里，10分钟有效
        redisUtils.set(Constants.User.KEY_CAPTCHA_CONTENT + "123456", content, 60 * 10);
        // 输出图片流
        specCaptcha.out(response.getOutputStream());
    }


    @Autowired
    private CommentDao commentDao;

    @Autowired
    private IUserService userService;

    @PostMapping("/comment")
    public ResponseResult testComment(@RequestBody Comment comment, HttpServletRequest request, HttpServletResponse response) {
        String content = comment.getContent();
        log.info("comment content == > " + content);
        //还得知道是谁的评论，对这个评论，身份进行确定
        String tokenKey = CookieUtils.getCookie(request, Constants.User.COOKIE_TOKE_KEY);
        if (tokenKey == null) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }

        SobUser sobUser = userService.checkSobUser();
        if (sobUser == null) {
            return ResponseResult.ACCOUNT_NOT_LOGIN();
        }
        comment.setUserId(sobUser.getId());
        comment.setUserAvatar(sobUser.getAvatar());
        comment.setUserName(sobUser.getUserName());
        comment.setCreateTime(new Date());
        comment.setUpdateTime(new Date());
        comment.setId(idWorker.nextId() + "");
        commentDao.save(comment);
        return ResponseResult.SUCCESS("评论成功.");
    }

    @Autowired
    private SolrTestService solrTestService;

    @PostMapping("/solr")
    public ResponseResult solrAddTest() {
        solrTestService.add();
        return ResponseResult.SUCCESS("添加成功.");
    }

    @PostMapping("/solr/all")
    public ResponseResult solrAddAllTest() {
        solrTestService.importAll();
        return ResponseResult.SUCCESS("添加全部成功.");
    }


    @PutMapping("/solr")
    public ResponseResult solrUpdateTest() {
        solrTestService.update();
        return ResponseResult.SUCCESS("更新成功.");
    }

    @DeleteMapping("/solr")
    public ResponseResult solrDeleteTest() {
        solrTestService.delete();
        return ResponseResult.SUCCESS("删除成功.");
    }

    @DeleteMapping("/solr/all")
    public ResponseResult solrDeleteAllTest() {
        solrTestService.deleteAll();
        return ResponseResult.SUCCESS("删除全部成功.");
    }
}
