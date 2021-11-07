package blog.system.server.entity;


import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tb_settings")
public class SettingsEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @ApiModelProperty("ID")
    @Column(name = "id", nullable = false)
    private String id;

    /**
     * 键
     */
    @ApiModelProperty("键")
    @Column(name = "key", nullable = false)
    private String key;

    /**
     * 值
     */
    @ApiModelProperty("值")
    @Column(name = "value", nullable = false)
    private String value;

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
    public SettingsEntity setId(String id) {
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
     * 键
     */
    public SettingsEntity setKey(String key) {
        this.key = key;
        return this;
    }

    /**
     * 键
     */
    public String getKey() {
        return key;
    }

    /**
     * 值
     */
    public SettingsEntity setValue(String value) {
        this.value = value;
        return this;
    }

    /**
     * 值
     */
    public String getValue() {
        return value;
    }

    /**
     * 创建时间
     */
    public SettingsEntity setCreateTime(Date createTime) {
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
    public SettingsEntity setUpdateTime(Date updateTime) {
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
        return "SettingsEntity{" +
                "id=" + id + '\'' +
                "key=" + key + '\'' +
                "value=" + value + '\'' +
                "createTime=" + createTime + '\'' +
                "updateTime=" + updateTime + '\'' +
                '}';
    }
}
