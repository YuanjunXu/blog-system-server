package blog.system.server.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tb_refresh_token")
public class RefreshTokenEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    /**
     * 刷新的token
     */
    @ApiModelProperty("刷新的token")
    @Column(name = "refresh_token")
    private String refreshToken;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    @ApiModelProperty("用户ID")
    private String userId;

    /**
     * token的key
     */
    @Column(name = "token_key")
    @ApiModelProperty("token的key")
    private String tokenKey;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 移动端的token_key
     */
    @Column(name = "mobile_token_key")
    @ApiModelProperty("移动端的token_key")
    private String mobileTokenKey;

    public RefreshTokenEntity setId(String id) {
        this.id = id;
        return this;
    }

    public String getId() {
        return id;
    }

    /**
     * 刷新的token
     */
    public RefreshTokenEntity setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    /**
     * 刷新的token
     */
    public String getRefreshToken() {
        return refreshToken;
    }

    /**
     * 用户ID
     */
    public RefreshTokenEntity setUserId(String userId) {
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
     * token的key
     */
    public RefreshTokenEntity setTokenKey(String tokenKey) {
        this.tokenKey = tokenKey;
        return this;
    }

    /**
     * token的key
     */
    public String getTokenKey() {
        return tokenKey;
    }

    /**
     * 创建时间
     */
    public RefreshTokenEntity setCreateTime(Date createTime) {
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
    public RefreshTokenEntity setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    /**
     * 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 移动端的token_key
     */
    public RefreshTokenEntity setMobileTokenKey(String mobileTokenKey) {
        this.mobileTokenKey = mobileTokenKey;
        return this;
    }

    /**
     * 移动端的token_key
     */
    public String getMobileTokenKey() {
        return mobileTokenKey;
    }

    @Override
    public String toString() {
        return "RefreshTokenEntity{" +
                "id=" + id + '\'' +
                "refreshToken=" + refreshToken + '\'' +
                "userId=" + userId + '\'' +
                "tokenKey=" + tokenKey + '\'' +
                "createTime=" + createTime + '\'' +
                "updateTime=" + updateTime + '\'' +
                "mobileTokenKey=" + mobileTokenKey + '\'' +
                '}';
    }
}
