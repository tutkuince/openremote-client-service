package com.tworun.openremoteclientservice.client;

import com.tworun.openremoteclientservice.config.FeignConfig;
import com.tworun.openremoteclientservice.dto.TokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "authClient",
        url = "${openremote.auth.url}",
        configuration = FeignConfig.class
)
public interface AuthClient {

    @PostMapping
    TokenResponse getToken(@RequestBody MultiValueMap<String, String> formData);
}
