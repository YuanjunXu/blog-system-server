package blog.system.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Description:
 *
 * @author 宣君
 * @date 2021-11-07 0:18
 */
@Configuration
public class Swagger2Configuration {

    @Value("${blog.swagger.enable}")
    private boolean enable;

    public static final String VERSION = "1.0.0";

    @Bean
    public Docket portalApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(portalApiInfo())
                .enable(enable)
                .select()
                .apis(RequestHandlerSelectors.basePackage("blog.system.server.controller.portal"))
                .paths(PathSelectors.any())
                .build()
                .groupName("门户站点");
    }
    private ApiInfo portalApiInfo() {
        return new ApiInfoBuilder()
                .title("宣君博客系统门户接口文档")
                .description("门户接口文档")
                .version(VERSION)
                .build();
    }


    @Bean
    public Docket adminApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(adminApiInfo())
                .enable(enable)
                .select()
                .apis(RequestHandlerSelectors.basePackage("blog.system.server.controller.admin"))
                .paths(PathSelectors.any())
                .build()
                .groupName("管理中心");
    }

    private ApiInfo adminApiInfo() {
        return new ApiInfoBuilder()
                .title("宣君博客系统后台管理中心接口文档")
                .description("管理中心接口")
                .version(VERSION)
                .build();
    }

    @Bean
    public Docket UserApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(userApiInfo())
                .enable(enable)
                .select()
                .apis(RequestHandlerSelectors.basePackage("blog.system.server.controller.user"))
                .paths(PathSelectors.any()) // 可以根据url路径设置哪些请求加入文档，忽略哪些请求
                .build()
                .groupName("用户中心");
    }

    private ApiInfo userApiInfo() {
        return new ApiInfoBuilder()
                .title("阳光沙滩博客系统用户接口") //设置文档的标题
                .description("用户接口的接口") // 设置文档的描述
                .version(VERSION) // 设置文档的版本信息-> 1.0.0 Version information
                .build();
    }
}
