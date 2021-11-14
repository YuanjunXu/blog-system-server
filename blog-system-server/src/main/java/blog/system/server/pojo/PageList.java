package blog.system.server.pojo;

import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

public class PageList<T> implements Serializable {


    public PageList() {

    }

    public PageList(long currentPage, long totalCount, long pageSize) {
        //1
        this.currentPage = currentPage;
        //17
        this.totalCount = totalCount;
        //10
        this.pageSize = pageSize;
        // 17/10 = 1 ==> 问题出现 17/10f = 1.7  == > 2
        //10 /10 = 1.0 => 1.0+0.9= 1.9 == > 1
        //11/10 = 1.1 => 1.1 + 0.9 = 2.0 == > 2
        //19/10 = 1.9 = > 1.9+0.9 = 2.8 = > 2
        //20/10 = > 2.0 => 2.0+0.9 = 2.9 => 2
        //21 == > 3
        //29 == > 3
        //30 == > 3
        //31== > 4
        this.totalPage = (long) (this.totalCount / (this.pageSize * 1.0f) + 0.9f);
        //计算总的页数
        //是否第一页/是否最后一页
        //第一页为0，最后一页为总的页码
        //10，每1页有10 == > 1
        //100,每1页有10 == > 10
        this.isFirst = this.currentPage == 1;
        this.isLast = this.currentPage == totalPage;
    }

    //做分页要多少数据
    //当前页码
    private long currentPage;
    //总数量
    private long totalCount;
    //每一页有多少数量
    private long pageSize;
    //总页数  = 总的数量/每页数量
    private long totalPage;
    //是否是第一页
    private boolean isFirst;
    //是否是最后一页
    private boolean isLast;
    //这是数据
    private List<T> contents;

    public long getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    public List<T> getContents() {
        return contents;
    }

    public void setContents(List<T> contents) {
        this.contents = contents;
    }

    public void parsePage(Page<T> all) {
        setContents(all.getContent());
        setFirst(all.isFirst());
        setLast(all.isLast());
        setCurrentPage(all.getNumber() + 1);
        setTotalCount(all.getTotalElements());
        setTotalPage(all.getTotalPages());
        setPageSize(all.getSize());
    }
}
