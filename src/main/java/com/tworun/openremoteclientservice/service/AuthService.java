package com.tworun.openremoteclientservice.service;

import com.tworun.openremoteclientservice.client.AuthClient;
import com.tworun.openremoteclientservice.constants.AuthConstants;
import com.tworun.openremoteclientservice.dto.TokenResponse;
import com.tworun.openremoteclientservice.exception.AccessTokenNotFoundException;
import com.tworun.openremoteclientservice.exception.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final AuthClient authClient;

    @Value("${openremote.client.id}")
    private String clientId;

    @Value("${openremote.client.secret}")
    private String clientSecret;

    public String getToken() {
        try {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add(AuthConstants.GRANT_TYPE_KEY, AuthConstants.GRANT_TYPE_CLIENT_CREDENTIALS);
            formData.add(AuthConstants.CLIENT_ID_KEY, clientId);
            formData.add(AuthConstants.CLIENT_SECRET_KEY, clientSecret);

            TokenResponse tokenResponse = authClient.getToken(formData);

            if (Objects.isNull(tokenResponse) || Objects.isNull(tokenResponse.getAccessToken())) {
                log.error("TokenResponse or AccessToken is null!");
                throw new AccessTokenNotFoundException("Could not obtain access token from Auth server: token is null");
            }

            return tokenResponse.getAccessToken();
        } catch (AccessTokenNotFoundException ex) {
            log.error("Access token not found: {}", ex.getMessage());
            throw ex;
        } catch (Exception e) {
            log.error("Failed to get access token from AuthClient: {}", e.getMessage(), e);
            throw new AuthException("Could not obtain access token from Auth server", e);
        }
    }
}
