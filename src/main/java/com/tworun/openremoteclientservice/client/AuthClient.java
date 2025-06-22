package com.tworun.openremoteclientservice.client;

import com.tworun.openremoteclientservice.config.FeignConfig;
import com.tworun.openremoteclientservice.dto.TokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * FeignClient for obtaining OAuth2 tokens from OpenRemote Keycloak Auth server.
 * Used to authenticate and retrieve a Bearer access token for further API requests.
 */
@FeignClient(
        name = "authClient",
        url = "${openremote.auth.url}",
        configuration = FeignConfig.class
)
public interface AuthClient {

    /**
     * Requests an access token using client credentials flow.
     *
     * @param formData form-encoded data including grant_type, client_id, client_secret, etc.
     * @return TokenResponse containing the access token and token details.
     */
    @PostMapping
    TokenResponse getToken(@RequestBody MultiValueMap<String, String> formData);
}
