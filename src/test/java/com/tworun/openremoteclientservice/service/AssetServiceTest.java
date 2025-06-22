package com.tworun.openremoteclientservice.service;

import com.tworun.openremoteclientservice.client.AssetClient;
import com.tworun.openremoteclientservice.dto.AssetCreateRequest;
import com.tworun.openremoteclientservice.dto.AssetResponse;
import com.tworun.openremoteclientservice.exception.AssetNotFoundException;
import com.tworun.openremoteclientservice.exception.AuthException;
import feign.FeignException;
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
    @DisplayName("Should throw RuntimeException when FeignException occurs on createAsset")
    void whenFeignExceptionOnCreateAsset_thenThrowsRuntimeException() {
        String fakeToken = "token";
        AssetCreateRequest req = new AssetCreateRequest();
        when(authService.getToken()).thenReturn(fakeToken);
        when(assetClient.createAsset(eq("Bearer " + fakeToken), eq(req)))
                .thenThrow(mock(FeignException.class));

        assertThatThrownBy(() -> assetService.createAsset(req))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to create asset");
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
    @DisplayName("Should throw AssetNotFoundException when asset does not exist (FeignException.NotFound)")
    void getAsset_notFound() {
        String assetId = "notfound";
        String token = "token";
        when(authService.getToken()).thenReturn(token);
        FeignException notFound = mock(FeignException.NotFound.class);
        when(assetClient.getAsset("Bearer " + token, assetId)).thenThrow(notFound);

        assertThatThrownBy(() -> assetService.getAsset(assetId))
                .isInstanceOf(AssetNotFoundException.class)
                .hasMessageContaining(assetId);
    }

    @Test
    @DisplayName("Should throw RuntimeException when generic FeignException occurs on getAsset")
    void getAsset_feignException() {
        String assetId = "abc123";
        String token = "token";
        when(authService.getToken()).thenReturn(token);
        FeignException genericFeign = mock(FeignException.class);
        when(assetClient.getAsset("Bearer " + token, assetId)).thenThrow(genericFeign);

        assertThatThrownBy(() -> assetService.getAsset(assetId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to retrieve asset");
    }

    @Test
    @DisplayName("Should update asset successfully")
    void updateAsset_success() {
        String assetId = "abc123";
        String token = "token";
        AssetCreateRequest req = new AssetCreateRequest();
        AssetResponse mockResponse = new AssetResponse();

        when(authService.getToken()).thenReturn(token);
        when(assetClient.updateAsset("Bearer " + token, assetId, req)).thenReturn(mockResponse);

        AssetResponse result = assetService.updateAsset(assetId, req);

        assertThat(result).isNotNull();
        verify(assetClient).updateAsset("Bearer " + token, assetId, req);
    }

    @Test
    @DisplayName("Should throw AssetNotFoundException when updating non-existent asset (FeignException.NotFound)")
    void updateAsset_notFound() {
        String assetId = "notfound";
        String token = "token";
        AssetCreateRequest req = new AssetCreateRequest();
        FeignException notFound = mock(FeignException.NotFound.class);

        when(authService.getToken()).thenReturn(token);
        when(assetClient.updateAsset("Bearer " + token, assetId, req)).thenThrow(notFound);

        assertThatThrownBy(() -> assetService.updateAsset(assetId, req))
                .isInstanceOf(AssetNotFoundException.class)
                .hasMessageContaining(assetId);
    }

    @Test
    @DisplayName("Should throw RuntimeException when generic FeignException occurs on updateAsset")
    void updateAsset_feignException() {
        String assetId = "abc123";
        String token = "token";
        AssetCreateRequest req = new AssetCreateRequest();
        FeignException genericFeign = mock(FeignException.class);

        when(authService.getToken()).thenReturn(token);
        when(assetClient.updateAsset("Bearer " + token, assetId, req)).thenThrow(genericFeign);

        assertThatThrownBy(() -> assetService.updateAsset(assetId, req))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to update asset");
    }
}