# 基于哪个镜像，如果不写版本则是最新的
# 不同版本的话要注意一下路径，下面的路径
FROM solr:5.5.5
# 作者
MAINTAINER xuyuanjun.cn
# 创建 Core，切换目录
WORKDIR /opt/solr/server/solr
# 创建目录
RUN mkdir blog_core
# 切换目录
WORKDIR /opt/solr/server/solr/blog_core
# 把这个Core名称添加到配置文件中，这样在前端就可以看到了
RUN echo 'name=blog_core' > core.properties
# 从模版中复制一份出来配置文件出来
RUN cp -r /opt/solr/server/solr/configsets/sample_techproducts_configs/conf/ .
# 进入到容器的lib文件夹
WORKDIR /opt/solr/server/solr-webapp/webapp/WEB-INF/lib
# 中文分词器
RUN cp -r /opt/solr/contrib/analysis-extras/lucene-libs/lucene-analyzers-smartcn-5.5.5.jar .
# 复制两个jar包进去
ADD ik-analyzer-solr5-5.x.jar .
ADD solr-analyzer-ik-5.1.0.jar .
WORKDIR /opt/solr/server/solr-webapp/webapp/WEB-INF
# 复制字典，同学们可以自行修改字典内容
ADD ext.dic .
ADD stopword.dic .
# 配置扩展字典的配置文件
ADD IKAnalyzer.cfg.xml .
# 增加分词配置
COPY managed-schema /opt/solr/server/solr/blog_core/conf
WORKDIR /opt/solr
