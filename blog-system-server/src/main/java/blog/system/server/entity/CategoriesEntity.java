package blog.system.server.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tb_categories")
public class CategoriesEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @ApiModelProperty("ID")
    @Column(name = "id", nullable = false)
    private String id;

    /**
     * 分类名称
     */
    @ApiModelProperty("分类名称")
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 拼音
     */
    @ApiModelProperty("拼音")
    @Column(name = "pinyin", nullable = false)
    private String pinyin;

    /**
     * 描述
     */
    @ApiModelProperty("描述")
    @Column(name = "description", nullable = false)
    private String description;

    /**
     * 顺序
     */
    @ApiModelProperty("顺序")
    @Column(name = "order", nullable = false)
    private Integer order;

    /**
     * 状态：0表示不使用，1表示正常
     */
    @ApiModelProperty("状态：0表示不使用，1表示正常")
    @Column(name = "status", nullable = false)
    private String status;

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
    public CategoriesEntity setId(String id) {
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
     * 分类名称
     */
    public CategoriesEntity setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 分类名称
     */
    public String getName() {
        return name;
    }

    /**
     * 拼音
     */
    public CategoriesEntity setPinyin(String pinyin) {
        this.pinyin = pinyin;
        return this;
    }

    /**
     * 拼音
     */
    public String getPinyin() {
        return pinyin;
    }

    /**
     * 描述
     */
    public CategoriesEntity setDescription(String description) {
        this.description = description;
        return this;
    }

    /**
     * 描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 顺序
     */
    public CategoriesEntity setOrder(Integer order) {
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
     * 状态：0表示不使用，1表示正常
     */
    public CategoriesEntity setStatus(String status) {
        this.status = status;
        return this;
    }

    /**
     * 状态：0表示不使用，1表示正常
     */
    public String getStatus() {
        return status;
    }

    /**
     * 创建时间
     */
    public CategoriesEntity setCreateTime(Date createTime) {
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
    public CategoriesEntity setUpdateTime(Date updateTime) {
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
        return "CategoriesEntity{" +
                "id=" + id + '\'' +
                "name=" + name + '\'' +
                "pinyin=" + pinyin + '\'' +
                "description=" + description + '\'' +
                "order=" + order + '\'' +
                "status=" + status + '\'' +
                "createTime=" + createTime + '\'' +
                "updateTime=" + updateTime + '\'' +
                '}';
    }
}
