package com.ming.zuul.gateway.conf;

import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Ming
 * @data: Create on 2019-06-03.
 */
@Configuration
@RefreshScope
@Log4j2
public class FilterConf {

    @Bean
    @RefreshScope
    public UrlCacheProFilter getUrlCacheProFilter() {
        log.info("zuul UrlCacheProFilter 相关属性配置成功！！！" );
        return new UrlCacheProFilter();
    }

    @Bean
    @RefreshScope
    public UrlCachePostFilter getUrlCachePostFilter() {
        log.info("zuul UrlCachePostFilter 相关属性配置成功！！！" );
        return new UrlCachePostFilter();
    }
}
