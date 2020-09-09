package com.iflytek.zhyl.valhalla;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableMethodCache(basePackages = "com.iflytek.zhyl.valhalla")
@EnableCreateCacheAnnotation
public class ValhallaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ValhallaApplication.class, args);
    }
}
