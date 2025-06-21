package com.tworun.openremoteclientservice.service;

import com.tworun.openremoteclientservice.client.AssetClient;
import com.tworun.openremoteclientservice.dto.AssetCreateRequest;
import com.tworun.openremoteclientservice.dto.AssetResponse;
import com.tworun.openremoteclientservice.exception.AssetNotFoundException;
import com.tworun.openremoteclientservice.exception.AuthException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssetServiceTest {

    @Mock
    private AssetClient assetClient;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AssetService assetService;

    @Test
    @DisplayName("Should call assetClient with correct params when creating asset")
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
    @DisplayName("Should throw AuthException when AuthService fails to get token")
    void whenAuthServiceFails_thenThrowsException() {
        when(authService.getToken()).thenThrow(new AuthException("Failed to get access token from AuthClient"));

        assertThrows(AuthException.class, () -> assetService.createAsset(new AssetCreateRequest()));
    }

    @Test
    @DisplayName("Should return asset when asset exists")
    void getAsset_success() {
        String assetId = "abc123";
        String token = "token";
        AssetResponse mockResponse = new AssetResponse();
        when(authService.getToken()).thenReturn(token);
        when(assetClient.getAsset("Bearer " + token, assetId)).thenReturn(mockResponse);

        AssetResponse result = assetService.getAsset(assetId);

        assertThat(result).isNotNull();
        verify(assetClient, times(1)).getAsset("Bearer " + token, assetId);
    }

    @Test
    @DisplayName("Should throw AssetNotFoundException when asset does not exist")
    void getAsset_notFound() {
        String assetId = "notfound";
        String token = "token";
        when(authService.getToken()).thenReturn(token);
        when(assetClient.getAsset("Bearer " + token, assetId))
                .thenThrow(new AssetNotFoundException("Asset not found with id: " + assetId));

        assertThatThrownBy(() -> assetService.getAsset(assetId))
                .isInstanceOf(AssetNotFoundException.class)
                .hasMessageContaining(assetId);
    }
}