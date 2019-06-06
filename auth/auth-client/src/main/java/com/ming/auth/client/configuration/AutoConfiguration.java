package com.ming.auth.client.configuration;

import com.ming.auth.client.config.ServiceAuthConfig;
import com.ming.auth.client.config.UserAuthConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by 2019-06-06
 */
@Configuration
@ComponentScan({"com.ming.auth.client", "com.ming.auth.common"})
public class AutoConfiguration {

    @Bean
    ServiceAuthConfig getServiceAuthConfig() {
        return new ServiceAuthConfig();
    }

    @Bean
    UserAuthConfig getUserAuthConfig() {
        return new UserAuthConfig();
    }

}
