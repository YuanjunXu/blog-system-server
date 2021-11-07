package blog.system.server.entity;


import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tb_comment")
public class CommentEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @ApiModelProperty("ID")
    @Column(name = "id", nullable = false)
    private String id;

    /**
     * 父内容
     */
    @ApiModelProperty("父内容")
    @Column(name = "parent_content")
    private String parentContent;

    /**
     * 文章ID
     */
    @ApiModelProperty("文章ID")
    @Column(name = "article_id", nullable = false)
    private String articleId;

    /**
     * 评论内容
     */
    @ApiModelProperty("评论内容")
    @Column(name = "content", nullable = false)
    private String content;

    /**
     * 评论用户的ID
     */
    @ApiModelProperty("评论用户的ID")
    @Column(name = "user_id", nullable = false)
    private String userId;

    /**
     * 评论用户的头像
     */
    @ApiModelProperty("评论用户的头像")
    @Column(name = "user_avatar")
    private String userAvatar;

    /**
     * 评论用户的名称
     */
    @Column(name = "user_name")
    @ApiModelProperty("评论用户的名称")
    private String userName;

    /**
     * 状态（0表示删除，1表示正常）
     */
    @ApiModelProperty("状态（0表示删除，1表示正常）")
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
    public CommentEntity setId(String id) {
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
     * 父内容
     */
    public CommentEntity setParentContent(String parentContent) {
        this.parentContent = parentContent;
        return this;
    }

    /**
     * 父内容
     */
    public String getParentContent() {
        return parentContent;
    }

    /**
     * 文章ID
     */
    public CommentEntity setArticleId(String articleId) {
        this.articleId = articleId;
        return this;
    }

    /**
     * 文章ID
     */
    public String getArticleId() {
        return articleId;
    }

    /**
     * 评论内容
     */
    public CommentEntity setContent(String content) {
        this.content = content;
        return this;
    }

    /**
     * 评论内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 评论用户的ID
     */
    public CommentEntity setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    /**
     * 评论用户的ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 评论用户的头像
     */
    public CommentEntity setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
        return this;
    }

    /**
     * 评论用户的头像
     */
    public String getUserAvatar() {
        return userAvatar;
    }

    /**
     * 评论用户的名称
     */
    public CommentEntity setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    /**
     * 评论用户的名称
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 状态（0表示删除，1表示正常）
     */
    public CommentEntity setState(String state) {
        this.state = state;
        return this;
    }

    /**
     * 状态（0表示删除，1表示正常）
     */
    public String getState() {
        return state;
    }

    /**
     * 创建时间
     */
    public CommentEntity setCreateTime(Date createTime) {
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
    public CommentEntity setUpdateTime(Date updateTime) {
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
        return "CommentEntity{" +
                "id=" + id + '\'' +
                "parentContent=" + parentContent + '\'' +
                "articleId=" + articleId + '\'' +
                "content=" + content + '\'' +
                "userId=" + userId + '\'' +
                "userAvatar=" + userAvatar + '\'' +
                "userName=" + userName + '\'' +
                "state=" + state + '\'' +
                "createTime=" + createTime + '\'' +
                "updateTime=" + updateTime + '\'' +
                '}';
    }
}
