package blog.system.server.serviceImpl;

import blog.system.server.dao.ArticleEntityRepository;
import blog.system.server.dao.ArticleNoContentEntityRepository;
import blog.system.server.dao.CommentEntityRepository;
import blog.system.server.dao.LabelsEntityRepository;
import blog.system.server.entity.ArticleEntity;
import blog.system.server.entity.ArticleNoContentEntity;
import blog.system.server.entity.LabelEntity;
import blog.system.server.entity.UserEntity;
import blog.system.server.service.IArticleService;
import blog.system.server.service.ISolrService;
import blog.system.server.service.IUserService;
import blog.system.server.utils.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vladsch.flexmark.ext.jekyll.tag.JekyllTagExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.SimTocExtension;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@Transactional
@Service
public class ArticleServiceImpl extends BaseService implements IArticleService {

    @Autowired
    private IUserService userService;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private ArticleEntityRepository articleDao;

    @Autowired
    private ArticleNoContentEntityRepository articleNoContentDao;

    @Autowired
    private ISolrService solrService;

    /**
     * 后期可以去做一些定时发布的功能
     * 如果是多人博客系统，得考虑审核的问题--->成功,通知，审核不通过，也可通知
     * <p>
     * 保存成草稿
     * 1、用户手动提交：会发生页面跳转-->提交完即可
     * 2、代码自动提交，每隔一段时间就会提交-->不会发生页面跳转-->多次提交-->如果没有唯一标识，会就重添加到数据库里
     * <p>
     * 不管是哪种草稿-->必须有标题
     * <p>
     * 方案一：每次用户发新文章之前-->先向后台请求一个唯一文章ID
     * 如果是更新文件，则不需要请求这个唯一的ID
     * <p>
     * 方案二：可以直接提交，后台判断有没有ID,如果没有ID，就新创建，并且ID作为此次返回的结果
     * 如果有ID，就修改已经存在的内容。
     * <p>
     * 推荐做法：
     * 自动保存草稿，在前端本地完成，也就是保存在本地。
     * 如果是用户手动提交的，就提交到后台
     *
     *
     * <p>
     * 防止重复提交（网络卡顿的时候，用户点了几次提交）：
     * 可以通过ID的方式
     * 通过token_key的提交频率来计算，如果30秒之内有多次提交，只有最前的一次有效
     * 其他的提交，直接return,提示用户不要太频繁操作.
     * <p>
     * 前端的处理：点击了提交以后，禁止按钮可以使用，等到有响应结果，再改变按钮的状态.
     *
     * @param article
     * @return
     */
    @Override
    public ResponseResult postArticle(ArticleEntity article) {
        //检查用户，获取到用户对象
        UserEntity sobUser = userService.checkSobUser();
        //未登录
        if (sobUser == null) {
            return new ResponseResult(HttpStatus.BAD_REQUEST, "用户未登录");
        }
        //检查数据
        //title、分类ID、内容、类型、摘要、标签
        String title = article.getTitle();
        if (TextUtils.isEmpty(title)) {
            return new ResponseResult(HttpStatus.BAD_REQUEST, "标题不可以为空.");
        }

        //2种，草稿和发布
        String state = article.getState();
        if (!Constants.Article.STATE_PUBLISH.equals(state) &&
                !Constants.Article.STATE_DRAFT.equals(state)) {
            //不支持此操作
            return new ResponseResult(HttpStatus.BAD_REQUEST, "不支持此操作");
        }

        String type = article.getType();
        if (TextUtils.isEmpty(type)) {
            return new ResponseResult(HttpStatus.BAD_REQUEST, "类型不可以为空.");
        }
        if (!"0".equals(type) && !"1".equals(type)) {
            return new ResponseResult(HttpStatus.BAD_REQUEST, "类型格式不对.");
        }

        //以下检查是发布的检查，草稿不需要检查
        if (Constants.Article.STATE_PUBLISH.equals(state)) {
            if (title.length() > Constants.Article.TITLE_MAX_LENGTH) {
                return new ResponseResult(HttpStatus.BAD_REQUEST, "文章标题不可以超过" + Constants.Article.TITLE_MAX_LENGTH + "个字符");
            }
            String content = article.getContent();
            if (TextUtils.isEmpty(content)) {
                return new ResponseResult(HttpStatus.BAD_REQUEST, "内容不可为空.");
            }

            String summary = article.getSummary();
            if (TextUtils.isEmpty(summary)) {
                return new ResponseResult(HttpStatus.BAD_REQUEST, "摘要不可以为空.");
            }
            if (summary.length() > Constants.Article.SUMMARY_MAX_LENGTH) {
                return new ResponseResult(HttpStatus.BAD_REQUEST, "摘要不可以超出" + Constants.Article.SUMMARY_MAX_LENGTH + "个字符.");
            }
            String labels = article.getLabel();
            //标签-标签1-标签2
            if (TextUtils.isEmpty(labels)) {
                return new ResponseResult(HttpStatus.BAD_REQUEST, "标签不可以为空.");
            }
        }

        String articleId = article.getId();
        if (TextUtils.isEmpty(articleId)) {
            //新内容,数据里没有的
            //补充数据：ID、创建时间、用户ID、更新时间
            article.setId(idWorker.nextId() + "");
            article.setCreateTime(new Date());
        } else {
            //更新内容，对状态进行处理，如果已经是发布的，则不能再保存为草稿
            ArticleEntity articleFromDb = articleDao.findOneById(articleId);
            if (Constants.Article.STATE_PUBLISH.equals(articleFromDb.getState()) &&
                    Constants.Article.STATE_DRAFT.equals(state)) {
                //已经发布了，只能更新，不能保存草稿
                return new ResponseResult(HttpStatus.BAD_REQUEST, "已发布文章不支持成为草稿.");
            }
        }
        article.setUserId(sobUser.getId());
        article.setUpdateTime(new Date());
        //保存到数据库里
        articleDao.save(article);
        if (Constants.Article.STATE_PUBLISH.equals(state)) {
            //保存到搜索的数据库里,应该正式发布才添加
            solrService.addArticle(article);
        }
        //打散标签，入库，统计
        this.setupLabels(article.getLabel());
        //删除文章列表
        redisUtils.del(Constants.Article.KEY_ARTICLE_LIST_FIRST_PAGE);
        //返回结果,只有一种case使用到这个ID
        //如果要做程序自动保存成草稿（比如说每30秒保存一次，就需要加上这个ID了，否则会创建多个Item）
        return new ResponseResult(HttpStatus.OK, Constants.Article.STATE_DRAFT.equals(state) ? "草稿保存成功" :
                "文章发表成功.", article.getId());
    }

    @Autowired
    private LabelsEntityRepository labelDao;

    private void setupLabels(String labels) {
        List<String> labelList = new ArrayList<>();
        if (labels.contains("-")) {
            labelList.addAll(Arrays.asList(labels.split("-")));
        } else {
            labelList.add(labels);
        }
        //入库，统计
        for (String label : labelList) {
            //找出来
            //Label targetLabel = labelDao.findOneByName(label);
            //if (targetLabel == null) {
            //    targetLabel = new Label();
            //    targetLabel.setId(idWorker.nextId() + "");
            //    targetLabel.setCount(0);
            //    targetLabel.setName(label);
            //    targetLabel.setCreateTime(new Date());
            //}
            //long count = targetLabel.getCount();
            //targetLabel.setCount(++count);
            //targetLabel.setUpdateTime(new Date());
            int result = labelDao.updateCountByName(label);
            if (result == 0) {
                LabelEntity targetLabel = new LabelEntity();
                targetLabel.setId(idWorker.nextId() + "");
                targetLabel.setCount(1);
                targetLabel.setName(label);
                targetLabel.setCreateTime(new Date());
                targetLabel.setUpdateTime(new Date());
                labelDao.save(targetLabel);
            }
        }
    }

    /**
     * 这里管理中，获取文章列表
     *
     * @param page       页码
     * @param size       每一页数量
     * @param keyword    标题关键字（搜索关键字）
     * @param categoryId 分类ID
     * @param state      状态：已经删除、草稿、已经发布的、置顶的
     * @return
     */
    @Override
    public ResponseResult<PageResult<ArticleNoContentEntity>> listArticles(int page, int size, String keyword,
                                                                           String categoryId, String state) {
        //处理一下size 和page
        page = checkPage(page);
        size = checkSize(size);
        //第一页数据做缓存
        String articleListJson = (String) redisUtils.get(Constants.Article.KEY_ARTICLE_LIST_FIRST_PAGE);
        boolean isSearch = !TextUtils.isEmpty(keyword) || !TextUtils.isEmpty(categoryId) || !TextUtils.isEmpty(state);
        if (!TextUtils.isEmpty(articleListJson) && page == 1 && !isSearch) {
            PageResult<ArticleNoContentEntity> result = gson.fromJson(articleListJson, new TypeToken<PageResult<ArticleNoContentEntity>>() {
            }.getType());
            log.info("article list first page from redis..");
            return new ResponseResult(HttpStatus.OK, "获取文章列表成功.", result);
        }
        //创建分页和排序条件
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        //开始查询
        Page<ArticleNoContentEntity> all = articleNoContentDao.findAll(new Specification<ArticleNoContentEntity>() {
            @Override
            public Predicate toPredicate(Root<ArticleNoContentEntity> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                //判断是否有传参数
                if (!TextUtils.isEmpty(state)) {
                    Predicate statePre = cb.equal(root.get("state").as(String.class), state);
                    predicates.add(statePre);
                }
                if (!TextUtils.isEmpty(categoryId)) {
                    Predicate categoryIdPre = cb.equal(root.get("categoryId").as(String.class), categoryId);
                    predicates.add(categoryIdPre);
                }
                if (!TextUtils.isEmpty(keyword)) {
                    Predicate titlePre = cb.like(root.get("title").as(String.class), "%" + keyword + "%");
                    predicates.add(titlePre);
                }
                Predicate[] preArray = new Predicate[predicates.size()];
                predicates.toArray(preArray);
                return cb.and(preArray);
            }
        }, pageable);
        //处理查询条件
        PageResult<ArticleNoContentEntity> result = new PageResult<>(all.getTotalElements(), all.getContent());
        //保存到redis里
        if (page == 1 && !isSearch) {
            redisUtils.set(Constants.Article.KEY_ARTICLE_LIST_FIRST_PAGE, gson.toJson(result), Constants.TimeValueInSecond.MIN_15);
        }
        //返回结果
        return new ResponseResult(HttpStatus.OK, "获取文章列表成功.", result);
    }

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private Gson gson;

    @Override
    public ResponseResult<ArticleEntity> getArticleByIdForAdmin(String articleId) {
        //查询出文章
        ArticleEntity article = articleDao.findOneById(articleId);
        if (article == null) {
            return new ResponseResult(HttpStatus.BAD_REQUEST, "文章不存在.");
        }
        //返回结果
        return new ResponseResult(HttpStatus.OK, "获取文章成功.", article);
    }

    /**
     * 如果有审核机制：审核中的文章-->只有管理员和作者自己可以获取
     * 有草稿、删除、置顶的、已经发布的
     * 删除的不能获取、其他都可以获取
     * <p>
     * 统计文章的阅读量：
     * 要精确一点的话要对IP进行处理，如果是同一个IP，则不保存
     * <p>
     * 先把阅读量在redis里更新
     * 文章也会在redis里缓存一份，比如说10分钟，
     * 当文章没的时候，从mysql中取，这个时同时更新阅读量
     * 10分以后，在下一次访问的更新一次阅读量
     *
     * @param articleId
     * @return
     */
    @Override
    public ResponseResult<ArticleEntity> getArticleById(String articleId) {
        //先从redis里获取文章
        //如果没有，再去mysql里获取
        String articleJson = (String) redisUtils.get(Constants.Article.KEY_ARTICLE_CACHE + articleId);
        if (!TextUtils.isEmpty(articleJson)) {
            log.info("article detail from redis ... ");
            ArticleEntity article = gson.fromJson(articleJson, ArticleEntity.class);
            //增加阅读数量
            redisUtils.incr(Constants.Article.KEY_ARTICLE_VIEW_COUNT + articleId, 1);
            return new ResponseResult(HttpStatus.BAD_REQUEST, "获取文章成功.", article);
        }

        //查询出文章
        ArticleEntity article = articleDao.findOneById(articleId);
        if (article == null) {
            return new ResponseResult(HttpStatus.BAD_REQUEST, "文章不存在.");
        }
        UserEntity sobUser = userService.checkSobUser();
        //判断文章状态
        String state = article.getState();
        if (Constants.Article.STATE_PUBLISH.equals(state) ||
                Constants.Article.STATE_TOP.equals(state)) {
            //处理文章内容
            String html;
            if (Constants.Article.TYPE_MARKDOWN.equals(article.getType())) {
                //转成html
                // markdown to html
                MutableDataSet options = new MutableDataSet().set(Parser.EXTENSIONS, Arrays.asList(
                        TablesExtension.create(),
                        JekyllTagExtension.create(),
                        TocExtension.create(),
                        SimTocExtension.create()
                ));
                Parser parser = Parser.builder(options).build();
                HtmlRenderer renderer = HtmlRenderer.builder(options).build();
                Node document = parser.parse(article.getContent());
                html = renderer.render(document);
            } else {
                html = article.getContent();
            }
            //复制一份文章
            String articleStr = gson.toJson(article);
            ArticleEntity newArticle = gson.fromJson(articleStr, ArticleEntity.class);
            newArticle.setContent(html);
            //正常发布的状态，才可以增加阅读量
            redisUtils.set(Constants.Article.KEY_ARTICLE_CACHE + articleId,
                    gson.toJson(newArticle), Constants.TimeValueInSecond.MIN_5);
            //设置阅读量的key，先从redis里拿，如果redis里没有，就article中获取，并且添加到redis里
            String viewCount = (String) redisUtils.get(Constants.Article.KEY_ARTICLE_VIEW_COUNT + articleId);
            if (TextUtils.isEmpty(viewCount)) {
                long newCount = article.getViewCount() + 1;
                redisUtils.set(Constants.Article.KEY_ARTICLE_VIEW_COUNT + articleId, String.valueOf(newCount));
            } else {
                //有的话就更新到mysql中
                long newCount = redisUtils.incr(Constants.Article.KEY_ARTICLE_VIEW_COUNT + articleId, 1);
                article.setViewCount(newCount);
                articleDao.save(article);
                //更新solr里的阅读量
                solrService.updateArticle(articleId, article);
            }
            //可以返回
            return new ResponseResult(HttpStatus.OK, "获取文章成功.", newArticle);
        }
        //如果是删除/草稿，需要管理角色
        if (sobUser == null || !Constants.User.ROLE_ADMIN.equals(sobUser.getRoles())) {
            return new ResponseResult(HttpStatus.BAD_REQUEST, "没有权限");
        }
        //返回结果
        return new ResponseResult(HttpStatus.OK, "获取文章成功.", article);
    }

    /**
     * 更新文章内容
     * <p>
     * 该接口只支持修改内容：标题、内容、标签、分类，摘要
     *
     * @param articleId 文章ID
     * @param article   文章
     * @return
     */
    @Override
    public ResponseResult updateArticle(String articleId, ArticleEntity article) {
        //先找出来
        ArticleEntity articleFromDb = articleDao.findOneById(articleId);
        if (articleFromDb == null) {
            return new ResponseResult(HttpStatus.BAD_REQUEST, "文章不存在.");
        }
        //内容修改
        String title = article.getTitle();
        if (!TextUtils.isEmpty(title)) {
            articleFromDb.setTitle(title);
        }

        String summary = article.getSummary();
        if (!TextUtils.isEmpty(summary)) {
            articleFromDb.setSummary(summary);
        }

        String content = article.getContent();
        if (!TextUtils.isEmpty(content)) {
            articleFromDb.setContent(content);
        }

        String label = article.getLabel();
        if (!TextUtils.isEmpty(label)) {
            articleFromDb.setLabel(label);
        }

        String state = article.getState();
        if (!TextUtils.isEmpty(state)) {
            articleFromDb.setState(state);
        }


        String categoryId = article.getCategoryId();
        if (!TextUtils.isEmpty(categoryId)) {
            articleFromDb.setCategoryId(categoryId);
        }
        articleFromDb.setCover(article.getCover());
        articleFromDb.setUpdateTime(new Date());
        articleDao.save(articleFromDb);
        redisUtils.del(Constants.Article.KEY_ARTICLE_CACHE + articleId, Constants.Article.KEY_ARTICLE_LIST_FIRST_PAGE);
        //返回结果
        return new ResponseResult(HttpStatus.OK, "文章更新成功.");
    }

    @Autowired
    private CommentEntityRepository commentDao;

    /**
     * 删除文章，物理删除
     *
     * @param articleId
     * @return
     */
    @Override
    public ResponseResult deleteArticleById(String articleId) {
        //要先把评论也删除了
        commentDao.deleteAllByArticleId(articleId);
        //因为评论的articleId，外键是article的Id
        int result = articleDao.deleteAllById(articleId);
        if (result > 0) {
            redisUtils.del(Constants.Article.KEY_ARTICLE_CACHE + articleId);
            redisUtils.del(Constants.Article.KEY_ARTICLE_LIST_FIRST_PAGE);
            //删除搜索库中的内容
            solrService.deleteArticle(articleId);
            return new ResponseResult(HttpStatus.OK, "文章删除成功.");
        }
        return new ResponseResult(HttpStatus.BAD_REQUEST, "文章不存在.");
    }

    /**
     * 通过修改状态删除文章，标记删除
     *
     * @param articleId
     * @return
     */
    @Override
    public ResponseResult deleteArticleByState(String articleId) {
        int result = articleDao.deleteArticleByState(articleId);
        if (result > 0) {
            redisUtils.del(Constants.Article.KEY_ARTICLE_CACHE + articleId);
            redisUtils.del(Constants.Article.KEY_ARTICLE_LIST_FIRST_PAGE);
            //删除搜索库中的内容
            solrService.deleteArticle(articleId);
            return new ResponseResult(HttpStatus.OK, "文章删除成功.");
        }
        return new ResponseResult(HttpStatus.BAD_REQUEST, "文章不存在.");
    }

    @Override
    public ResponseResult topArticle(String articleId) {
        //必须已经发布的，才可以置顶
        ArticleEntity article = articleDao.findOneById(articleId);
        if (article == null) {
            return new ResponseResult(HttpStatus.BAD_REQUEST, "文章不存在");
        }
        String state = article.getState();
        if (Constants.Article.STATE_PUBLISH.equals(state)) {
            article.setState(Constants.Article.STATE_TOP);
            articleDao.save(article);
            redisUtils.del(Constants.Article.KEY_ARTICLE_LIST_FIRST_PAGE);
            return new ResponseResult(HttpStatus.OK, "文章置顶成功.");
        }
        if (Constants.Article.STATE_TOP.equals(state)) {
            article.setState(Constants.Article.STATE_PUBLISH);
            articleDao.save(article);
            redisUtils.del(Constants.Article.KEY_ARTICLE_LIST_FIRST_PAGE);
            return new ResponseResult(HttpStatus.OK, "已取消置顶.");
        }
        return new ResponseResult(HttpStatus.BAD_REQUEST, "不支持该操作.");
    }

    /**
     * 获取置顶文章
     * 跟权限无关
     * 状态必须置顶
     *
     * @return
     */
    @Override
    public ResponseResult<List<ArticleNoContentEntity>> listTopArticles() {
        List<ArticleNoContentEntity> result = articleNoContentDao.findAll(new Specification<ArticleNoContentEntity>() {
            @Override
            public Predicate toPredicate(Root<ArticleNoContentEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return criteriaBuilder.equal(root.get("state").as(String.class), Constants.Article.STATE_TOP);
            }
        });
        return new ResponseResult(HttpStatus.OK, "获取置顶文章列表成功.", result);
    }

    @Autowired
    private Random random;

    /**
     * 获取推荐文章，通过标签来计算
     *
     * @param articleId
     * @param size
     * @return
     */
    @Override
    public ResponseResult<List<ArticleNoContentEntity>> listRecommendArticle(String articleId, int size) {
        //查询文章，不需要文章，只需要标签
        String labels = articleDao.listArticleLabelsById(articleId);
        //打散标签
        List<String> labelList = new ArrayList<>();
        if (!labels.contains("-")) {
            labelList.add(labels);
        } else {
            labelList.addAll(Arrays.asList(labels.split("-")));
        }
        //从列表中随即获取一标签，查询与此标签相似的文章
        String targetLabel = labelList.get(random.nextInt(labelList.size()));
        log.info("targetLabel == > " + targetLabel);
        List<ArticleNoContentEntity> likeResultList = articleNoContentDao.listArticleByLikeLabel("%" + targetLabel + "%", articleId, size);
        //判断它的长度
        if (likeResultList.size() < size) {
            //说明不够数量，获取最新的文章作为补充
            int dxSize = size - likeResultList.size();
            List<ArticleNoContentEntity> dxList = articleNoContentDao.listLastedArticleBySize(articleId, dxSize);
            //这个写法有一定的弊端，会把可能前面找到的也加进来，概率比较小，如果文章比较多
            likeResultList.addAll(dxList);
        }
        return new ResponseResult(HttpStatus.OK, "获取推荐文章成功.", likeResultList);
    }

    @Override
    public ResponseResult<PageResult<ArticleNoContentEntity>> listArticlesByLabel(int page, int size, String label) {
        page = checkPage(page);
        size = checkSize(size);
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<ArticleNoContentEntity> all = articleNoContentDao.findAll(new Specification<ArticleNoContentEntity>() {
            @Override
            public Predicate toPredicate(Root<ArticleNoContentEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Predicate labelPre = criteriaBuilder.like(root.get("label").as(String.class), "%" + label + "%");
                Predicate statePublishPre = criteriaBuilder.equal(root.get("state").as(String.class), Constants.Article.STATE_PUBLISH);
                Predicate stateTopPre = criteriaBuilder.equal(root.get("state").as(String.class), Constants.Article.STATE_TOP);
                Predicate or = criteriaBuilder.or(statePublishPre, stateTopPre);
                return criteriaBuilder.and(or, labelPre);
            }
        }, pageable);
        PageResult<ArticleNoContentEntity> pageResult = new PageResult<>(all.getTotalElements(), all.getContent());
        return new ResponseResult(HttpStatus.OK, "获取文章列表成功.", pageResult);
    }

    @Override
    public ResponseResult<PageResult<LabelEntity>> listLabels(int size) {
        size = this.checkSize(size);
        Sort sort = new Sort(Sort.Direction.DESC, "count");
        Pageable pageable = PageRequest.of(0, size, sort);
        Page<LabelEntity> all = labelDao.findAll(pageable);
        PageResult<LabelEntity> pageResult = new PageResult<>(all.getTotalElements(), all.getContent());
        return new ResponseResult(HttpStatus.OK, "获取标签列表成功.", pageResult);
    }

    @Override
    public ResponseResult<Long> getArticleCount() {
        long count = articleDao.count();
        return new ResponseResult(HttpStatus.OK,"文章总数获取成功.",count);
    }
}
