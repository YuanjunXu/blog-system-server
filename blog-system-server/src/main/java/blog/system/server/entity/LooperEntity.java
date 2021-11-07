package blog.system.server.entity;


import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tb_looper")
public class LooperEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @ApiModelProperty("ID")
    @Column(name = "id", nullable = false)
    private String id;

    /**
     * 轮播图标题
     */
    @ApiModelProperty("轮播图标题")
    @Column(name = "title", nullable = false)
    private String title;

    /**
     * 顺序
     */
    @ApiModelProperty("顺序")
    @Column(name = "order", nullable = false)
    private Integer order = 0;

    /**
     * 状态：0表示不可用，1表示正常
     */
    @ApiModelProperty("状态：0表示不可用，1表示正常")
    @Column(name = "state", nullable = false)
    private String state;

    /**
     * 目标URL
     */
    @ApiModelProperty("目标URL")
    @Column(name = "target_url")
    private String targetUrl;

    /**
     * 图片地址
     */
    @ApiModelProperty("图片地址")
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

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
    public LooperEntity setId(String id) {
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
     * 轮播图标题
     */
    public LooperEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 轮播图标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 顺序
     */
    public LooperEntity setOrder(Integer order) {
        this.order = order;
        return this;
    }

    /**
     * 顺序
     */
    public Integer getOrder() {
        return order;
    }

    /**
     * 状态：0表示不可用，1表示正常
     */
    public LooperEntity setState(String state) {
        this.state = state;
        return this;
    }

    /**
     * 状态：0表示不可用，1表示正常
     */
    public String getState() {
        return state;
    }

    /**
     * 目标URL
     */
    public LooperEntity setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
        return this;
    }

    /**
     * 目标URL
     */
    public String getTargetUrl() {
        return targetUrl;
    }

    /**
     * 图片地址
     */
    public LooperEntity setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    /**
     * 图片地址
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * 创建时间
     */
    public LooperEntity setCreateTime(Date createTime) {
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
    public LooperEntity setUpdateTime(Date updateTime) {
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
        return "LooperEntity{" +
                "id=" + id + '\'' +
                "title=" + title + '\'' +
                "order=" + order + '\'' +
                "state=" + state + '\'' +
                "targetUrl=" + targetUrl + '\'' +
                "imageUrl=" + imageUrl + '\'' +
                "createTime=" + createTime + '\'' +
                "updateTime=" + updateTime + '\'' +
                '}';
    }
}
