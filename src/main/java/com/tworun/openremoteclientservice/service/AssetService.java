package com.tworun.openremoteclientservice.service;

import com.tworun.openremoteclientservice.client.AssetClient;
import com.tworun.openremoteclientservice.dto.AssetCreateRequest;
import com.tworun.openremoteclientservice.dto.AssetResponse;
import com.tworun.openremoteclientservice.exception.AssetNotFoundException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetClient assetClient;
    private final AuthService authService;

    public AssetResponse createAsset(AssetCreateRequest request) {
        log.info("Creating asset with name: {}, type: {}", request.getName(), request.getType());
        String token = authService.getToken();
        String authHeader = "Bearer " + token;
        AssetResponse response = assetClient.createAsset(authHeader, request);
        log.info("Asset created with id: {}", response.getId());
        return response;
    }

    public AssetResponse getAsset(String assetId) {
        log.info("Retrieving asset with id: {}", assetId);
        try {
            String token = authService.getToken();
            String authHeader = "Bearer " + token;
            AssetResponse response = assetClient.getAsset(authHeader, assetId);
            log.info("Asset retrieved: {}", response.getId());
            return response;
        } catch (FeignException.NotFound ex) {
            log.warn("Asset not found with id: {}", assetId);
            throw new AssetNotFoundException("Asset not found with id: " + assetId);
        }
    }

}
