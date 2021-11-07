package blog.system.server.utils;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 *
 * @author 宣君
 * @date 2021-11-07 0:52
 */
public class PageResult <T> implements Serializable {
    private long totalCount;
    private List<T> data;

    public PageResult(long totalCount, List<T> data) {
        this.totalCount = totalCount;
        this.data = data;
    }

    public PageResult() {
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
