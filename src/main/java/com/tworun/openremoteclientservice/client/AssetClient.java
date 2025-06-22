package com.tworun.openremoteclientservice.client;

import com.tworun.openremoteclientservice.dto.AssetCreateRequest;
import com.tworun.openremoteclientservice.dto.AssetResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * FeignClient for interacting with the OpenRemote Asset REST API.
 * Handles CRUD operations on IoT assets.
 */
@FeignClient(name = "assetClient", url = "${openremote.api.baseurl}")
public interface AssetClient {

    /**
     * Creates a new asset in OpenRemote.
     *
     * @param accessToken Bearer access token for authorization (e.g., "Bearer eyJ...").
     * @param assetCreateRequest Asset creation request payload.
     * @return AssetResponse with created asset details.
     * @throws feign.FeignException if the API call fails (handled in service).
     */
    @PostMapping(
            value = "/asset",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    AssetResponse createAsset(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody AssetCreateRequest assetCreateRequest
    );

    /**
     * Retrieves an asset by its unique id.
     *
     * @param accessToken Bearer access token.
     * @param assetId Asset's unique identifier.
     * @return AssetResponse with asset details.
     */
    @GetMapping("/asset/{assetId}")
    AssetResponse getAsset(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("assetId") String assetId
    );

    /**
     * Updates an existing asset with the given id.
     *
     * @param token Bearer access token.
     * @param assetId Id of the asset to update.
     * @param request Asset update payload (same as creation payload).
     * @return Updated asset's details.
     */
    @PutMapping("/asset/{assetId}")
    AssetResponse updateAsset(
            @RequestHeader("Authorization") String token,
            @PathVariable("assetId") String assetId,
            @RequestBody AssetCreateRequest request
    );

    /**
     * Deletes one or more assets by id.
     *
     * @param authHeader Bearer access token.
     * @param assetIds List of asset ids to delete.
     */
    @DeleteMapping(value = "/asset")
    void deleteAssets(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("assetId") List<String> assetIds
    );
}
