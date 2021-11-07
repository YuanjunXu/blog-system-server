package blog.system.server.entity;


import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tb_daily_view_count")
public class DailyViewCountEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @ApiModelProperty("ID")
    @Column(name = "id", nullable = false)
    private String id;

    /**
     * 每天浏览量
     */
    @ApiModelProperty("每天浏览量")
    @Column(name = "view_count", nullable = false)
    private Integer viewCount = 0;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @Column(name = "create_time", nullable = false)
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    @Column(name = "update_time", nullable = false)
    private Date updateTime;

    /**
     * ID
     */
    public DailyViewCountEntity setId(String id) {
        this.id = id;
        return this;
    }

    /**
     * ID
     */
    public String getId() {
        return id;
    }

    /**
     * 每天浏览量
     */
    public DailyViewCountEntity setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
        return this;
    }

    /**
     * 每天浏览量
     */
    public Integer getViewCount() {
        return viewCount;
    }

    /**
     * 创建时间
     */
    public DailyViewCountEntity setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    /**
     * 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 更新时间
     */
    public DailyViewCountEntity setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    /**
     * 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public String toString() {
        return "DailyViewCountEntity{" +
                "id=" + id + '\'' +
                "viewCount=" + viewCount + '\'' +
                "createTime=" + createTime + '\'' +
                "updateTime=" + updateTime + '\'' +
                '}';
    }
}
