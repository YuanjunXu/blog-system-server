package blog.system.server.entity;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "tb_article")
public class ArticleEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @ApiModelProperty("ID")
    @Column(name = "id", nullable = false)
    private String id;

    /**
     * 标题
     */
    @ApiModelProperty("标题")
    @Column(name = "title", nullable = false)
    private String title;

    /**
     * 用户ID
     */
    @ApiModelProperty("用户ID")
    @Column(name = "user_id", nullable = false)
    private String userId;

    /**
     * 用户头像
     */
    @ApiModelProperty("用户头像")
    @Column(name = "user_avatar")
    private String userAvatar;

    /**
     * 用户昵称
     */
    @ApiModelProperty("用户昵称")
    @Column(name = "user_name")
    private String userName;

    /**
     * 分类ID
     */
    @ApiModelProperty("分类ID")
    @Column(name = "category_id", nullable = false)
    private String categoryId;

    /**
     * 文章内容
     */
    @ApiModelProperty("文章内容")
    @Column(name = "content", nullable = false)
    private String content;

    /**
     * 类型（0表示富文本，1表示markdown）
     */
    @Column(name = "type", nullable = false)
    @ApiModelProperty("类型（0表示富文本，1表示markdown）")
    private String type;

    /**
     * 状态（0表示已发布，1表示草稿，2表示删除）
     */
    @Column(name = "state", nullable = false)
    @ApiModelProperty("状态（0表示已发布，1表示草稿，2表示删除）")
    private String state;

    /**
     * 摘要
     */
    @ApiModelProperty("摘要")
    @Column(name = "summary", nullable = false)
    private String summary;

    /**
     * 标签
     */
    @ApiModelProperty("标签")
    @Column(name = "labels", nullable = false)
    private String label;

    /**
     * 阅读数量
     */
    @ApiModelProperty("阅读数量")
    @Column(name = "view_count", nullable = false)
    private long viewCount = 0;

    /**
     * 发布时间
     */
    @ApiModelProperty("发布时间")
    @Column(name = "create_time", nullable = false)
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    @Column(name = "update_time", nullable = false)
    private Date updateTime;

    /**
     * 封面
     */
    @ApiModelProperty("封面")
    @Column(name = "cover")
    private String cover;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    /**
     * ID
     */
    public ArticleEntity setId(String id) {
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
     * 标题
     */
    public ArticleEntity setTitle(String title) {
        this.title = title;
        return this;
    }

    /**
     * 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 用户ID
     */
    public ArticleEntity setUserId(String userId) {
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
     * 用户头像
     */
    public ArticleEntity setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
        return this;
    }

    /**
     * 用户头像
     */
    public String getUserAvatar() {
        return userAvatar;
    }

    /**
     * 用户昵称
     */
    public ArticleEntity setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    /**
     * 用户昵称
     */
    public String getUserName() {
        return userName;
    }

    /**
     * 分类ID
     */
    public ArticleEntity setCategoryId(String categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    /**
     * 分类ID
     */
    public String getCategoryId() {
        return categoryId;
    }

    /**
     * 文章内容
     */
    public ArticleEntity setContent(String content) {
        this.content = content;
        return this;
    }

    /**
     * 文章内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 类型（0表示富文本，1表示markdown）
     */
    public ArticleEntity setType(String type) {
        this.type = type;
        return this;
    }

    /**
     * 类型（0表示富文本，1表示markdown）
     */
    public String getType() {
        return type;
    }

    /**
     * 状态（0表示已发布，1表示草稿，2表示删除）
     */
    public ArticleEntity setState(String state) {
        this.state = state;
        return this;
    }

    /**
     * 状态（0表示已发布，1表示草稿，2表示删除）
     */
    public String getState() {
        return state;
    }

    /**
     * 摘要
     */
    public ArticleEntity setSummary(String summary) {
        this.summary = summary;
        return this;
    }

    /**
     * 摘要
     */
    public String getSummary() {
        return summary;
    }

    /**
     * 标签
     */
    public ArticleEntity setLabels(String label) {
        this.label = label;
        return this;
    }

    /**
     * 标签
     */
    public String getLabels() {
        return label;
    }

    /**
     * 阅读数量
     */
    public ArticleEntity setViewCount(long viewCount) {
        this.viewCount = viewCount;
        return this;
    }

    /**
     * 阅读数量
     */
    public long getViewCount() {
        return viewCount;
    }

    /**
     * 发布时间
     */
    public ArticleEntity setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    /**
     * 发布时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 更新时间
     */
    public ArticleEntity setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    /**
     * 更新时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    @Transient
    private List<String> labels = new ArrayList<>();

    @Override
    public String toString() {
        return "ArticleEntity{" +
                "id=" + id + '\'' +
                "title=" + title + '\'' +
                "userId=" + userId + '\'' +
                "userAvatar=" + userAvatar + '\'' +
                "userName=" + userName + '\'' +
                "categoryId=" + categoryId + '\'' +
                "content=" + content + '\'' +
                "type=" + type + '\'' +
                "state=" + state + '\'' +
                "summary=" + summary + '\'' +
                "labels=" + labels + '\'' +
                "viewCount=" + viewCount + '\'' +
                "createTime=" + createTime + '\'' +
                "updateTime=" + updateTime + '\'' +
                '}';
    }

    public String getLabel() {
        //打散到集合里
        this.labels.clear();
        if (this.label != null) {
            if (!this.label.contains("-")) {
                this.labels.add(this.label);
            } else {
                String[] split = this.label.split("-");
                List<String> strings = Arrays.asList(split);
                this.labels.addAll(strings);
            }
        }
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
