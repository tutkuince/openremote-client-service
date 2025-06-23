package com.tworun.openremoteclientservice.service;


import com.tworun.openremoteclientservice.client.AuthClient;
import com.tworun.openremoteclientservice.dto.TokenResponse;
import com.tworun.openremoteclientservice.exception.AccessTokenNotFoundException;
import com.tworun.openremoteclientservice.exception.AuthException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthClient authClient;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = spy(new AuthService(authClient));

        ReflectionTestUtils.setField(authService, "clientId", "test-client");
        ReflectionTestUtils.setField(authService, "clientSecret", "test-secret");
    }

    @Test
    @DisplayName("Should return access token when AuthClient returns valid response")
    void shouldReturnAccessToken_whenTokenResponseIsValid() {
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken("mock-access-token");
        when(authClient.getToken(any(MultiValueMap.class))).thenReturn(tokenResponse);

        String result = authService.getToken();

        assertEquals("mock-access-token", result);
    }

    @Test
    @DisplayName("Should throw AuthException when AuthClient throws any exception")
    void shouldThrowAuthException_whenAuthClientThrows() {
        when(authClient.getToken(any(MultiValueMap.class))).thenThrow(new AuthException("connection error"));

        AuthException ex = assertThrows(AuthException.class, () -> authService.getToken());
        assertTrue(ex.getMessage().contains("connection error"));
    }

    @Test
    void shouldThrowAuthException_whenAccessTokenIsNull() {
        TokenResponse tokenResponse = new TokenResponse();
        tokenResponse.setAccessToken(null);
        when(authClient.getToken(any(MultiValueMap.class))).thenReturn(tokenResponse);

        assertThrows(AccessTokenNotFoundException.class, () -> authService.getToken());
    }

    @Test
    @DisplayName("getTokenFallback should throw AuthException with correct message and cause")
    void getTokenFallback_shouldThrowAuthException() {
        RuntimeException originalException = new RuntimeException("Original error from AuthClient");

        AuthException thrownException = assertThrows(AuthException.class,
                () -> ReflectionTestUtils.invokeMethod(authService, "getTokenFallback", originalException)
        );

        assertThat(thrownException)
                .hasMessage("Auth service temporarily unavailable.")
                .hasCauseInstanceOf(RuntimeException.class)
                .hasCause(originalException);
    }
}