package blog.system.server.controller.admin;

import blog.system.server.entity.LooperEntity;
import blog.system.server.interceptor.CheckTooFrequentCommit;
import blog.system.server.service.ILoopService;
import blog.system.server.utils.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "轮播图")
@RestController
@RequestMapping("/admin/loop")
public class LooperAdminApi {

    @Autowired
    private ILoopService loopService;

    @ApiOperation("添加轮播图")
    @CheckTooFrequentCommit
    @PreAuthorize("@permission.admin()")
    @PostMapping
    public ResponseResult addLoop(@RequestBody LooperEntity looper) {
        return loopService.addLoop(looper);
    }

    @ApiOperation("删除轮播图")
    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{loopId}")
    public ResponseResult deleteLoop(@PathVariable("loopId") String loopId) {
        return loopService.deleteLoop(loopId);
    }

    @ApiOperation("更新轮播图")
    @CheckTooFrequentCommit
    @PreAuthorize("@permission.admin()")
    @PutMapping("/{loopId}")
    public ResponseResult updateLoop(@PathVariable("loopId") String loopId, @RequestBody LooperEntity looper) {
        return loopService.updateLoop(loopId,looper);
    }

    @ApiOperation("获取轮播图")
    @PreAuthorize("@permission.admin()")
    @GetMapping("/{loopId}")
    public ResponseResult getLoop(@PathVariable("loopId") String loopId) {
        return loopService.getLoop(loopId);
    }

    @ApiOperation("轮播图列表")
    @PreAuthorize("@permission.admin()")
    @GetMapping("/list")
    public ResponseResult listLoops() {
        return loopService.listLoops();
    }
}
