package blog.system.server.controller.user;


import blog.system.server.dao.LabelsEntityRepository;
import blog.system.server.serviceImpl.SolrTestService;
import blog.system.server.utils.IdWorker;
import blog.system.server.utils.RedisUtils;
import blog.system.server.utils.ResponseResult;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

//
//@Transactional
@Slf4j
@Api(tags = "测试")
@RestController
public class TestController {


    @Autowired
    private IdWorker idWorker;

    @Autowired
    private LabelsEntityRepository labelDao;


    @Autowired
    private RedisUtils redisUtils;


    @Autowired
    private SolrTestService solrTestService;

    @PostMapping("/solr")
    public ResponseResult solrAddTest() {
        solrTestService.add();
        return new ResponseResult(HttpStatus.OK, "添加成功.");
    }

    @PostMapping("/solr/all")
    public ResponseResult solrAddAllTest() {
        solrTestService.importAll();
        return new ResponseResult(HttpStatus.OK, "全部添加成功.");
    }


    @PutMapping("/solr")
    public ResponseResult solrUpdateTest() {
        solrTestService.update();
        return new ResponseResult(HttpStatus.OK, "更新成功.");
    }

    @DeleteMapping("/solr")
    public ResponseResult solrDeleteTest() {
        solrTestService.delete();
        return new ResponseResult(HttpStatus.OK, "删除成功.");
    }

    @DeleteMapping("/solr/all")
    public ResponseResult solrDeleteAllTest() {
        solrTestService.deleteAll();
        return new ResponseResult(HttpStatus.OK, "删除全部成功.");
    }
}
