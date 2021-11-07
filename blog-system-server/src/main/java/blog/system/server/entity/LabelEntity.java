package blog.system.server.entity;


import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tb_labels")
public class LabelEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @ApiModelProperty("ID")
    @Column(name = "id", nullable = false)
    private String id;

    /**
     * 标签名称
     */
    @ApiModelProperty("标签名称")
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * 数量
     */
    @ApiModelProperty("数量")
    @Column(name = "count", nullable = false)
    private Integer count = 0;

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
    public LabelEntity setId(String id) {
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
     * 标签名称
     */
    public LabelEntity setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * 标签名称
     */
    public String getName() {
        return name;
    }

    /**
     * 数量
     */
    public LabelEntity setCount(Integer count) {
        this.count = count;
        return this;
    }

    /**
     * 数量
     */
    public Integer getCount() {
        return count;
    }

    /**
     * 创建时间
     */
    public LabelEntity setCreateTime(Date createTime) {
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
    public LabelEntity setUpdateTime(Date updateTime) {
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
        return "LabelsEntity{" +
                "id=" + id + '\'' +
                "name=" + name + '\'' +
                "count=" + count + '\'' +
                "createTime=" + createTime + '\'' +
                "updateTime=" + updateTime + '\'' +
                '}';
    }
}
