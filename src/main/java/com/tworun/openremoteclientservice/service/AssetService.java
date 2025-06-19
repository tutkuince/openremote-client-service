package com.tworun.openremoteclientservice.service;

import com.tworun.openremoteclientservice.client.AssetClient;
import com.tworun.openremoteclientservice.dto.AssetCreateRequest;
import com.tworun.openremoteclientservice.dto.AssetResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetClient assetClient;
    private final AuthService authService;

    public AssetResponse createAsset(AssetCreateRequest request) {
        String token = authService.getToken();
        String authHeader = "Bearer " + token;
        return assetClient.createAsset(authHeader, request);
    }
}
