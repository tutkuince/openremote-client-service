package com.tworun.openremoteclientservice.config;

import feign.Client;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;

/**
 * Custom Feign configuration that disables SSL certificate and hostname verification.
 * <p>
 * Only use this in development or test environments where you need to trust self-signed certificates
 * (for example, when running OpenRemote with a self-signed SSL cert).
 * <br/>
 * Do NOT use this configuration in production, as it makes your HTTP client vulnerable to man-in-the-middle attacks!
 * </p>
 */
@Configuration
public class FeignConfig {

    /**
     * Creates a Feign Client that trusts all SSL certificates and disables hostname verification.
     *
     * @return a custom Feign Client for use with self-signed or invalid SSL certificates
     * @throws Exception if SSL context cannot be created
     */
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
