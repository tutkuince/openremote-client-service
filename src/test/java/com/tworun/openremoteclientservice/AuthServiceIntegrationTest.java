package com.tworun.openremoteclientservice;

import com.tworun.openremoteclientservice.exception.AuthException;
import com.tworun.openremoteclientservice.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AuthServiceIntegrationTest {

    @Autowired
    private AuthService authService;

    @Test
    void getToken_shouldCallFallback_onException() {
        assertThrows(AuthException.class, () -> authService.getToken());
    }
}
