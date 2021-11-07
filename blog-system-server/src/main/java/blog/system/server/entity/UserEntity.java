package blog.system.server.entity;


import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tb_user")
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @ApiModelProperty("ID")
    @Column(name = "id", nullable = false)
    private String id;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    @Column(name = "user_name", nullable = false)
    private String userName;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * 角色
     */
    @ApiModelProperty("角色")
    @Column(name = "roles", nullable = false)
    private String roles;

    /**
     * 头像地址
     */
    @ApiModelProperty("头像地址")
    @Column(name = "avatar", nullable = false)
    private String avatar;

    /**
     * 邮箱地址
     */
    @Column(name = "email")
    @ApiModelProperty("邮箱地址")
    private String email;

    /**
     * 签名
     */
    @Column(name = "sign")
    @ApiModelProperty("签名")
    private String sign;

    /**
     * 状态：0表示删除，1表示正常
     */
    @ApiModelProperty("状态：0表示删除，1表示正常")
    @Column(name = "state", nullable = false)
    private String state;

    /**
     * 注册ip
     */
    @ApiModelProperty("注册ip")
    @Column(name = "reg_ip", nullable = false)
    private String regIp;

    /**
     * 登录Ip
     */
    @ApiModelProperty("登录Ip")
    @Column(name = "login_ip", nullable = false)
    private String loginIp;

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
    public UserEntity setId(String id) {
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
     * 用户名
     */
    public UserEntity setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    /**
     * 用户名
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 密码
     */
    public UserEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    /**
     * 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 角色
     */
    public UserEntity setRoles(String roles) {
        this.roles = roles;
        return this;
    }

    /**
     * 角色
     */
    public String getRoles() {
        return roles;
    }

    /**
     * 头像地址
     */
    public UserEntity setAvatar(String avatar) {
        this.avatar = avatar;
        return this;
    }

    /**
     * 头像地址
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * 邮箱地址
     */
    public UserEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    /**
     * 邮箱地址
     */
    public String getEmail() {
        return email;
    }

    /**
     * 签名
     */
    public UserEntity setSign(String sign) {
        this.sign = sign;
        return this;
    }

    /**
     * 签名
     */
    public String getSign() {
        return sign;
    }

    /**
     * 状态：0表示删除，1表示正常
     */
    public UserEntity setState(String state) {
        this.state = state;
        return this;
    }

    /**
     * 状态：0表示删除，1表示正常
     */
    public String getState() {
        return state;
    }

    /**
     * 注册ip
     */
    public UserEntity setRegIp(String regIp) {
        this.regIp = regIp;
        return this;
    }

    /**
     * 注册ip
     */
    public String getRegIp() {
        return regIp;
    }

    /**
     * 登录Ip
     */
    public UserEntity setLoginIp(String loginIp) {
        this.loginIp = loginIp;
        return this;
    }

    /**
     * 登录Ip
     */
    public String getLoginIp() {
        return loginIp;
    }

    /**
     * 创建时间
     */
    public UserEntity setCreateTime(Date createTime) {
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
    public UserEntity setUpdateTime(Date updateTime) {
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
        return "UserEntity{" +
                "id=" + id + '\'' +
                "userName=" + userName + '\'' +
                "password=" + password + '\'' +
                "roles=" + roles + '\'' +
                "avatar=" + avatar + '\'' +
                "email=" + email + '\'' +
                "sign=" + sign + '\'' +
                "state=" + state + '\'' +
                "regIp=" + regIp + '\'' +
                "loginIp=" + loginIp + '\'' +
                "createTime=" + createTime + '\'' +
                "updateTime=" + updateTime + '\'' +
                '}';
    }
}
