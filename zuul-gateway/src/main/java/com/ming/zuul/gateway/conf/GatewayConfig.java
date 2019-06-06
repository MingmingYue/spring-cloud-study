package com.ming.zuul.gateway.conf;

import com.ming.zuul.gateway.handler.RequestBodyRoutePredicateFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Ming
 * @data: Create on 2019-06-05.
 */
@Configuration
public class GatewayConfig {

    @Bean
    RequestBodyRoutePredicateFactory requestBodyRoutePredicateFactory() {
        return new RequestBodyRoutePredicateFactory();
    }
}
