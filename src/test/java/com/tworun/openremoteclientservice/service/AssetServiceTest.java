package com.tworun.openremoteclientservice.service;

import com.tworun.openremoteclientservice.client.AssetClient;
import com.tworun.openremoteclientservice.dto.AssetCreateRequest;
import com.tworun.openremoteclientservice.dto.AssetResponse;
import com.tworun.openremoteclientservice.exception.AuthException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssetServiceTest {

    @Mock
    private AssetClient assetClient;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AssetService assetService;

    @Test
    void whenCreateAssetCalled_thenAssetClientCalledWithCorrectParams() {
        String fakeToken = "fake-token";
        String fakeHeader = "Bearer " + fakeToken;
        AssetCreateRequest req = new AssetCreateRequest();
        AssetResponse expectedResponse = new AssetResponse();

        when(authService.getToken()).thenReturn(fakeToken);
        when(assetClient.createAsset(eq(fakeHeader), eq(req))).thenReturn(expectedResponse);

        AssetResponse actualResponse = assetService.createAsset(req);

        assertEquals(expectedResponse, actualResponse);
        verify(authService).getToken();
        verify(assetClient).createAsset(fakeHeader, req);
    }

    @Test
    void whenAuthServiceFails_thenThrowsException() {
        when(authService.getToken()).thenThrow(new AuthException("Failed to get access token from AuthClient"));

        assertThrows(AuthException.class, () -> assetService.createAsset(new AssetCreateRequest()));
    }
}