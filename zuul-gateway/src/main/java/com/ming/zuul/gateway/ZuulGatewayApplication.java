package com.ming.zuul.gateway;

import com.ming.auth.client.EnableAuthClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Created by 2019-06-03
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableAuthClient
@EnableFeignClients({"com.ming.auth.client.feign","com.ming.zuul.gateway.feign"})
public class ZuulGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZuulGatewayApplication.class, args);
    }
}
