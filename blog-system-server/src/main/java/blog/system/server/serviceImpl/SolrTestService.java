package blog.system.server.serviceImpl;

import blog.system.server.dao.ArticleEntityRepository;
import blog.system.server.entity.ArticleEntity;
import blog.system.server.utils.Constants;
import com.vladsch.flexmark.ext.jekyll.tag.JekyllTagExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.toc.SimTocExtension;
import com.vladsch.flexmark.ext.toc.TocExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class SolrTestService {

    @Autowired
    private SolrClient solrClient;

    public void add() {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", "20173013961932801");
        doc.addField("blog_view_count", 10);
        doc.addField("blog_title", "这篇文章的标题被我修改过了,哈哈，呵呵大笑！");
        doc.addField("blog_content", "<p>这是我写的android文章，是一篇不错的文章，欢迎大家点赞，三连：投币、点赞、收藏、转发...</p>\n");
        doc.addField("blog_create_time", new Date());
        doc.addField("blog_labels", "测试-博客");
        doc.addField("blog_url", "https://www.sunofbeach.net");
        doc.addField("blog_category_id", "234123143");
        try {
            solrClient.add(doc);
            solrClient.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update() {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", "725138215987576832");
        doc.addField("view_count", 10);
        doc.addField("title", "哈哈，呵呵大笑！");
        doc.addField("content", "这是我写的android文章，是一篇不错的文章，欢迎大家点赞，三连：投币、点赞、收藏、转发...");
        doc.addField("create_time", new Date());
        doc.addField("labels", "测试-博客");
        doc.addField("url", "https://www.sunofbeach.net");
        doc.addField("category_id", "234123143");
        try {
            solrClient.add(doc);
            solrClient.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        try {
            //单独删除一条一记录
            solrClient.deleteById("725138215987576832");
            //删除所有
            //solrClient.deleteByQuery("*");
            solrClient.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAll() {
        try {
            //单独删除一条一记录
            //solrClient.deleteById("725138215987576832");
            //删除所有
            solrClient.deleteByQuery("*");
            solrClient.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Autowired
    private ArticleEntityRepository articleDao;

    public void importAll() {
        List<ArticleEntity> all = articleDao.findAll();
        for (ArticleEntity article : all) {
            if (Constants.Article.STATE_DRAFT.equals(article.getState()) ||
                    Constants.Article.STATE_DELETE.equals(article.getState())) {
                continue;
            }
            SolrInputDocument doc = new SolrInputDocument();
            doc.addField("id", article.getId());
            doc.addField("blog_view_count", article.getViewCount());
            doc.addField("blog_title", article.getTitle());
            //对内容进行处理，去掉标签，提取出纯文本
            //第一种是由markdown写的内容--->type = 1
            //第二种是符文本内容 === > type = 0
            //如果type === 1 ===> 转成html
            //再由html === > 纯文本
            //如果type == 0 == > 纯文本
            String type = article.getType();
            String html;
            if (Constants.Article.TYPE_MARKDOWN.equals(type)) {
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
            //到这里,不管原来是什么,现在都是Html
            //html== > text
            String content = Jsoup.parse(html).text();
            doc.addField("blog_content", content);
            doc.addField("blog_create_time", article.getCreateTime());
            doc.addField("blog_labels", article.getLabel());
            doc.addField("blog_url", "/article/" + article.getId());
            doc.addField("blog_category_id", article.getCategoryId());
            try {
                solrClient.add(doc);
                solrClient.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
