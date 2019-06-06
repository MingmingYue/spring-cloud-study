package com.ming.auth.client.interceptor;

import com.ming.auth.client.config.ServiceAuthConfig;
import com.ming.auth.client.config.UserAuthConfig;
import com.ming.auth.client.jwt.ServiceAuthUtil;
import com.ming.common.constant.CommonConstants;
import com.ming.common.context.BaseContextHandler;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

import lombok.extern.log4j.Log4j2;

/**
 * Created by 2019-06-06
 */
@Log4j2
@Component
public class OkHttpTokenInterceptor implements Interceptor {

    private ServiceAuthUtil serviceAuthUtil;
    private ServiceAuthConfig serviceAuthConfig;
    private UserAuthConfig userAuthConfig;


    public OkHttpTokenInterceptor(@Autowired @Lazy ServiceAuthUtil serviceAuthUtil,
                                  @Autowired @Lazy ServiceAuthConfig serviceAuthConfig,
                                  @Autowired @Lazy UserAuthConfig userAuthConfig) {
    }

    @Override
    public Response intercept(okhttp3.Interceptor.Chain chain) throws IOException {
        Request newRequest = null;
        if (chain.request().url().toString().contains("client/token")) {
            newRequest = chain.request()
                    .newBuilder()
                    .header(userAuthConfig.getTokenHeader(), BaseContextHandler.getToken())
                    .build();
        } else {
            newRequest = chain.request()
                    .newBuilder()
                    .header(userAuthConfig.getTokenHeader(), BaseContextHandler.getToken())
                    .header(serviceAuthConfig.getTokenHeader(), serviceAuthUtil.getClientToken())
                    .build();
        }
        Response response = chain.proceed(newRequest);
        if (HttpStatus.FORBIDDEN.value() == response.code()) {
            if (response.body().string().contains(String.valueOf(CommonConstants.EX_CLIENT_INVALID_CODE))) {
                log.info("Client Token Expire,Retry to request...");
                serviceAuthUtil.refreshClientToken();
                newRequest = chain.request()
                        .newBuilder()
                        .header(userAuthConfig.getTokenHeader(), BaseContextHandler.getToken())
                        .header(serviceAuthConfig.getTokenHeader(), serviceAuthUtil.getClientToken())
                        .build();
                response = chain.proceed(newRequest);
            }
        }
        return response;
    }
}
