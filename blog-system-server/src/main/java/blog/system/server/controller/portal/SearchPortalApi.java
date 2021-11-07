package blog.system.server.controller.portal;

import blog.system.server.dao.SearchResultEntity;
import blog.system.server.service.ISolrService;
import blog.system.server.utils.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "检索")
@RestController
@RequestMapping("/portal/search")
public class SearchPortalApi {

    @Autowired
    private ISolrService solrService;

    @ApiOperation("检索")
    @GetMapping
    public ResponseResult<List<SearchResultEntity>> doSearch(@RequestParam("keyword") String keyword,
                                                             @RequestParam("page") int page,
                                                             @RequestParam("size") int size,
                                                             @RequestParam(value = "categoryId", required = false) String categoryId,
                                                             @RequestParam(value = "sort", required = false) Integer sort) {
        return solrService.doSearch(keyword, page, size, categoryId, sort);
    }
}
