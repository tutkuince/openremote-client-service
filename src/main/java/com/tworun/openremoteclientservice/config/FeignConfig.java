package com.tworun.openremoteclientservice.config;

import feign.Client;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;


@Configuration
public class FeignConfig {

    @Bean
    public Client feignClient() throws Exception {
        SSLContext sslContext = new SSLContextBuilder()
                .loadTrustMaterial(null, (certificate, authType) -> true)   // Trust all certificates
                .build();

        return new Client.Default(
                sslContext.getSocketFactory(),
                new NoopHostnameVerifier()                                          // Also turn off hostname verification
        );
    }
}
