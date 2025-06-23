package com.tworun.openremoteclientservice.controller;

import com.tworun.openremoteclientservice.dto.AssetCreateRequest;
import com.tworun.openremoteclientservice.dto.AssetResponse;
import com.tworun.openremoteclientservice.service.AssetService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing IoT assets via OpenRemote.
 * <p>
 * Provides endpoints to create, retrieve, update and delete assets.
 * All endpoints require a valid authentication token.
 * </p>
 */
@Tag(name = "Asset", description = "API for managing assets")
@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
@Slf4j
public class AssetController {

    private final AssetService assetService;

    /**
     * Creates a new asset.
     *
     * @param request Asset creation request payload.
     * @return The created AssetResponse.
     */
    @Operation(
            summary = "Create asset",
            description = "Creates a new asset and returns the created asset info."
    )
    @ApiResponse(responseCode = "201", description = "Asset successfully created")
    @RateLimiter(name = "assetCreationLimiter")
    @PostMapping
    public ResponseEntity<AssetResponse> createAsset(@RequestBody @Valid AssetCreateRequest request) {
        return new ResponseEntity<>(assetService.createAsset(request), HttpStatus.CREATED);
    }

    /**
     * Retrieves an asset by its ID.
     *
     * @param assetId The ID of the asset to retrieve.
     * @return The AssetResponse for the given asset ID.
     */
    @Operation(
            summary = "Get asset by ID",
            description = "Retrieves an asset by its unique identifier."
    )
    @ApiResponse(responseCode = "200", description = "Asset found and returned")
    @ApiResponse(responseCode = "404", description = "Asset not found")
    @RateLimiter(name = "assetRetrievalLimiter")
    @GetMapping("/{assetId}")
    public ResponseEntity<AssetResponse> getAsset(@PathVariable String assetId) {
        return ResponseEntity.ok(assetService.getAsset(assetId));
    }

    /**
     * Updates an existing asset.
     *
     * @param assetId The ID of the asset to update.
     * @param request Updated asset data.
     * @return The updated AssetResponse.
     */
    @Operation(
            summary = "Update asset",
            description = "Updates an existing asset with the given ID."
    )
    @ApiResponse(responseCode = "200", description = "Asset updated successfully")
    @ApiResponse(responseCode = "404", description = "Asset not found")
    @RateLimiter(name = "assetUpdateLimiter")
    @PutMapping("/{assetId}")
    public ResponseEntity<AssetResponse> updateAsset(
            @PathVariable String assetId,
            @RequestBody @Valid AssetCreateRequest request) {
        AssetResponse updated = assetService.updateAsset(assetId, request);
        return ResponseEntity.ok(updated);
    }

    /**
     * Deletes one or more assets by their IDs.
     *
     * @param assetIds List of asset IDs to delete.
     * @return 204 No Content if successful.
     */
    @Operation(
            summary = "Delete assets",
            description = "Deletes one or more assets by their IDs. Returns 204 No Content if deletion is successful."
    )
    @ApiResponse(responseCode = "204", description = "Assets deleted successfully")
    @ApiResponse(responseCode = "404", description = "One or more assets not found")
    @RateLimiter(name = "assetDeleteLimiter")
    @DeleteMapping
    public ResponseEntity<Void> deleteAssets(@RequestBody List<String> assetIds) {
        assetService.deleteAssets(assetIds);
        return ResponseEntity.noContent().build();
    }

}
