package blog.system.server.entity;


import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tb_friends")
public class FriendsEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @ApiModelProperty("ID")
    @Column(name = "id", nullable = false)
    private String id;

    /**
     * 友情链接名称
     */
    @ApiModelProperty("友情链接名称")
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 友情链接logo
     */
    @ApiModelProperty("友情链接logo")
    @Column(name = "logo", nullable = false)
    private String logo;

    /**
     * 友情链接
     */
    @ApiModelProperty("友情链接")
    @Column(name = "url", nullable = false)
    private String url;

    /**
     * 顺序
     */
    @ApiModelProperty("顺序")
    @Column(name = "order", nullable = false)
    private Integer order = 0;

    /**
     * 友情链接状态:0表示不可用，1表示正常
     */
    @ApiModelProperty("友情链接状态:0表示不可用，1表示正常")
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
     * ID
     */
    public FriendsEntity setId(String id) {
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
     * 友情链接名称
     */
    public FriendsEntity setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 友情链接名称
     */
    public String getName() {
        return name;
    }

    /**
     * 友情链接logo
     */
    public FriendsEntity setLogo(String logo) {
        this.logo = logo;
        return this;
    }

    /**
     * 友情链接logo
     */
    public String getLogo() {
        return logo;
    }

    /**
     * 友情链接
     */
    public FriendsEntity setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * 友情链接
     */
    public String getUrl() {
        return url;
    }

    /**
     * 顺序
     */
    public FriendsEntity setOrder(Integer order) {
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
     * 友情链接状态:0表示不可用，1表示正常
     */
    public FriendsEntity setState(String state) {
        this.state = state;
        return this;
    }

    /**
     * 友情链接状态:0表示不可用，1表示正常
     */
    public String getState() {
        return state;
    }

    /**
     * 创建时间
     */
    public FriendsEntity setCreateTime(Date createTime) {
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
    public FriendsEntity setUpdateTime(Date updateTime) {
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
        return "FriendsEntity{" +
                "id=" + id + '\'' +
                "name=" + name + '\'' +
                "logo=" + logo + '\'' +
                "url=" + url + '\'' +
                "order=" + order + '\'' +
                "state=" + state + '\'' +
                "createTime=" + createTime + '\'' +
                "updateTime=" + updateTime + '\'' +
                '}';
    }
}
