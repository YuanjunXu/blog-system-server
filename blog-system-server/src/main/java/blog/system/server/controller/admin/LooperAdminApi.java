package blog.system.server.controller.admin;

import blog.system.server.interceptor.CheckTooFrequentCommit;
import blog.system.server.pojo.Looper;
import blog.system.server.response.ResponseResult;
import blog.system.server.services.ILoopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/loop")
public class LooperAdminApi {

    @Autowired
    private ILoopService loopService;

    @CheckTooFrequentCommit
    @PreAuthorize("@permission.admin()")
    @PostMapping
    public ResponseResult addLoop(@RequestBody Looper looper) {
        return loopService.addLoop(looper);
    }

    @PreAuthorize("@permission.admin()")
    @DeleteMapping("/{loopId}")
    public ResponseResult deleteLoop(@PathVariable("loopId") String loopId) {
        return loopService.deleteLoop(loopId);
    }

    @CheckTooFrequentCommit
    @PreAuthorize("@permission.admin()")
    @PutMapping("/{loopId}")
    public ResponseResult updateLoop(@PathVariable("loopId") String loopId, @RequestBody Looper looper) {
        return loopService.updateLoop(loopId,looper);
    }


    @PreAuthorize("@permission.admin()")
    @GetMapping("/{loopId}")
    public ResponseResult getLoop(@PathVariable("loopId") String loopId) {
        return loopService.getLoop(loopId);
    }


    @PreAuthorize("@permission.admin()")
    @GetMapping("/list")
    public ResponseResult listLoops() {
        return loopService.listLoops();
    }
}
