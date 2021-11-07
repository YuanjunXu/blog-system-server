package blog.system.server;

import blog.system.server.utils.IdWorker;
import blog.system.server.utils.RedisUtils;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Random;

/**
 * Description:
 *
 * @author 宣君
 * @date 2021-11-06 23:52
 */

@Slf4j
@EnableSwagger2
@SpringBootApplication
public class BlogApplication {


    public static void main(String[] args) {
        log.info("BlogApplication run...");
        SpringApplication.run(BlogApplication.class, args);

    }

    @Bean
    public IdWorker createIdWorker() {
        return new IdWorker(0, 0);
    }

    @Bean
    public BCryptPasswordEncoder createPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RedisUtils createRedisUtils() {
        return new RedisUtils();
    }

    @Bean
    public Random createRandom() {
        return new Random();
    }

    @Bean
    public Gson createGson() {
        return new Gson();
    }
}
