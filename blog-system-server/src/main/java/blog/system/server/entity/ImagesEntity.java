package blog.system.server.entity;


import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tb_images")
public class ImagesEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @ApiModelProperty("ID")
    @Column(name = "id", nullable = false)
    private String id;

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    @Column(name = "user_id", nullable = false)
    private String userId;

    /**
     * 路径URL
     */
    @ApiModelProperty("url")
    @Column(name = "url", nullable = false)
    private String url;

    /**
     * 路径
     */
    @ApiModelProperty("路径")
    @Column(name = "path")
    private String path;

    /**
     *类型
     */
    @ApiModelProperty("类型")
    @Column(name = "content_type")
    private String contentType;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    @Column(name = "name")
    private String name;

    /**
     * 状态（0表示删除，1表正常）
     */
    @ApiModelProperty("状态（0表示删除，1表正常）")
    @Column(name = "state", nullable = false)
    private String state;

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
     * 顺序
     */
    @ApiModelProperty("更新时间")
    @Column(name = "original")
    private String original;

    /**
     * ID
     */
    public ImagesEntity setId(String id) {
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
     * 用户ID
     */
    public ImagesEntity setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    /**
     * 用户ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 路径
     */
    public ImagesEntity setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * 路径
     */
    public String getUrl() {
        return url;
    }

    /**
     * 状态（0表示删除，1表正常）
     */
    public ImagesEntity setState(String state) {
        this.state = state;
        return this;
    }

    /**
     * 状态（0表示删除，1表正常）
     */
    public String getState() {
        return state;
    }

    /**
     * 创建时间
     */
    public ImagesEntity setCreateTime(Date createTime) {
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
    public ImagesEntity setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }


    /**
     * 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    @Override
    public String toString() {
        return "ImagesEntity{" +
                "id=" + id + '\'' +
                "userId=" + userId + '\'' +
                "url=" + url + '\'' +
                "state=" + state + '\'' +
                "createTime=" + createTime + '\'' +
                "updateTime=" + updateTime + '\'' +
                '}';
    }
}
