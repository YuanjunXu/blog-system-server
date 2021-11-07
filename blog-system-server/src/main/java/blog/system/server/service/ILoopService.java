package blog.system.server.service;

import blog.system.server.entity.LooperEntity;
import blog.system.server.utils.ResponseResult;

import java.util.List;

public interface ILoopService {
    ResponseResult addLoop(LooperEntity looper);

    ResponseResult<LooperEntity> getLoop(String loopId);

    ResponseResult<List<LooperEntity>> listLoops();

    ResponseResult updateLoop(String loopId, LooperEntity looper);

    ResponseResult deleteLoop(String loopId);
}
