package com.tworun.openremoteclientservice.service;

import com.tworun.openremoteclientservice.client.AuthClient;
import com.tworun.openremoteclientservice.constants.AuthConstants;
import com.tworun.openremoteclientservice.dto.TokenResponse;
import com.tworun.openremoteclientservice.exception.AccessTokenNotFoundException;
import com.tworun.openremoteclientservice.exception.AuthException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Objects;

/**
 * Service for obtaining OAuth2 access tokens from the OpenRemote Auth Server.
 * Wraps token request logic, error handling, and configuration.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private final AuthClient authClient;

    @Value("${openremote.client.id}")
    private String clientId;

    @Value("${openremote.client.secret}")
    private String clientSecret;

    private static final String AUTH_SERVICE = "authService";

    /**
     * Requests and returns an OAuth2 access token from the OpenRemote Auth server.
     * Handles any client or server errors and throws custom exceptions as needed.
     *
     * @return Access token string to be used as Bearer token for OpenRemote API calls.
     * @throws AccessTokenNotFoundException if token response is null or access token is missing.
     * @throws AuthException                for all other errors encountered during token retrieval.
     */
    @Retry(name = AUTH_SERVICE, fallbackMethod = "getTokenFallback")
    @CircuitBreaker(name = AUTH_SERVICE, fallbackMethod = "getTokenFallback")
    public String getToken() {
        log.info("Attempting to get token from Auth server...");
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add(AuthConstants.GRANT_TYPE_KEY, AuthConstants.GRANT_TYPE_CLIENT_CREDENTIALS);
        formData.add(AuthConstants.CLIENT_ID_KEY, clientId);
        formData.add(AuthConstants.CLIENT_SECRET_KEY, clientSecret);

        TokenResponse tokenResponse = authClient.getToken(formData);

        if (Objects.isNull(tokenResponse) || Objects.isNull(tokenResponse.getAccessToken())) {
            log.error("TokenResponse or AccessToken is null!");
            throw new AccessTokenNotFoundException("Could not obtain access token from Auth server: token is null");
        }
        log.info("Successfully obtained access token.");
        return tokenResponse.getAccessToken();
    }

    /**
     * Fallback method for getToken().
     * This method is called if the main getToken() method fails after retries or if the circuit is open.
     */
    private String getTokenFallback(Throwable t) {
        log.error("Fallback method called for getToken. Error: {}", t.getMessage());
        log.warn("Resilience4j handled the error and called fallback. Type: {}", t.getClass().getSimpleName()); // Yeni log
        throw new AuthException("Auth service temporarily unavailable.", t);
    }
}
