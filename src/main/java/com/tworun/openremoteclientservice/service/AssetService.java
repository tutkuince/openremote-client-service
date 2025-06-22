package com.tworun.openremoteclientservice.service;

import com.tworun.openremoteclientservice.client.AssetClient;
import com.tworun.openremoteclientservice.dto.AssetCreateRequest;
import com.tworun.openremoteclientservice.dto.AssetResponse;
import com.tworun.openremoteclientservice.exception.AssetNotFoundException;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer responsible for asset operations using the OpenRemote API.
 * Handles create, retrieve, update, and delete operations with proper logging and error handling.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetClient assetClient;
    private final AuthService authService;

    /**
     * Creates a new asset using the provided request details.
     *
     * @param request Asset creation request DTO.
     * @return The created asset response.
     * @throws RuntimeException if creation fails due to client or remote errors.
     */
    public AssetResponse createAsset(AssetCreateRequest request) {
        log.info("Creating asset with name: {}, type: {}", request.getName(), request.getType());
        try {
            String token = authService.getToken();
            String authHeader = "Bearer " + token;
            AssetResponse response = assetClient.createAsset(authHeader, request);
            log.info("Asset created with id: {}", response.getId());
            return response;
        } catch (FeignException ex) {
            log.error("Failed to create asset with name: {} - {}", request.getName(), ex.getMessage());
            throw new RuntimeException("Failed to create asset: " + ex.getMessage(), ex);
        }
    }

    /**
     * Retrieves asset details for the given asset ID.
     *
     * @param assetId ID of the asset to retrieve.
     * @return The asset response.
     * @throws AssetNotFoundException if the asset is not found.
     * @throws RuntimeException for other Feign client errors.
     */
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
        } catch (FeignException ex) {
            log.error("Feign error when getting asset: {}", ex.getMessage(), ex);
            throw new RuntimeException("Failed to retrieve asset: " + ex.getMessage(), ex);
        }
    }

    /**
     * Updates an existing asset with the specified asset ID and new details.
     *
     * @param assetId The ID of the asset to update.
     * @param request The updated asset details.
     * @return The updated asset response.
     * @throws AssetNotFoundException if the asset is not found.
     * @throws RuntimeException for other Feign client errors.
     */
    public AssetResponse updateAsset(String assetId, AssetCreateRequest request) {
        log.info("Updating asset with id: {} (name: {}, type: {})", assetId, request.getName(), request.getType());
        try {
            String token = authService.getToken();
            String authHeader = "Bearer " + token;
            AssetResponse response = assetClient.updateAsset(authHeader, assetId, request);
            log.info("Asset updated: {}", response.getId());
            return response;
        } catch (FeignException.NotFound ex) {
            log.warn("Asset not found with id: {}", assetId);
            throw new AssetNotFoundException("Asset not found with id: " + assetId);
        } catch (FeignException ex) {
            log.error("Feign error when updating asset: {}", ex.getMessage());
            throw new RuntimeException("Failed to update asset: " + ex.getMessage(), ex);
        }
    }

    /**
     * Deletes one or more assets by their IDs.
     *
     * @param assetIds List of asset IDs to delete.
     * @throws AssetNotFoundException if some or all assets are not found.
     * @throws RuntimeException for other Feign client errors.
     */
    public void deleteAssets(List<String> assetIds) {
        log.info("Deleting assets with ids: {}", assetIds);
        try {
            String token = authService.getToken();
            String authHeader = "Bearer " + token;
            assetClient.deleteAssets(authHeader, assetIds);
            log.info("Assets deleted: {}", assetIds);
        } catch (FeignException.BadRequest ex) {
            log.warn("Some asset(s) not found for ids: {}", assetIds);
            throw new AssetNotFoundException("Some asset(s) not found: " + assetIds);
        } catch (FeignException ex) {
            log.error("Feign error when deleting asset(s): {}", ex.getMessage());
            throw new RuntimeException("Failed to delete asset(s): " + ex.getMessage(), ex);
        }
    }

}
