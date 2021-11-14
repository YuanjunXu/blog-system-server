package blog.system.server.services;

import blog.system.server.pojo.Looper;
import blog.system.server.response.ResponseResult;

public interface ILoopService {
    ResponseResult addLoop(Looper looper);

    ResponseResult getLoop(String loopId);

    ResponseResult listLoops();

    ResponseResult updateLoop(String loopId, Looper looper);

    ResponseResult deleteLoop(String loopId);
}
