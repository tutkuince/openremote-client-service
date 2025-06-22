package com.tworun.openremoteclientservice.controller;

import com.tworun.openremoteclientservice.dto.AssetCreateRequest;
import com.tworun.openremoteclientservice.dto.AssetResponse;
import com.tworun.openremoteclientservice.service.AssetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
@Slf4j
public class AssetController {

    private final AssetService assetService;

    @PostMapping
    public ResponseEntity<AssetResponse> createAsset(@RequestBody @Valid AssetCreateRequest request) {
        return new ResponseEntity<>(assetService.createAsset(request), HttpStatus.CREATED);
    }

    @GetMapping("/{assetId}")
    public ResponseEntity<AssetResponse> getAsset(@PathVariable String assetId) {
        return ResponseEntity.ok(assetService.getAsset(assetId));
    }

    @PutMapping("/{assetId}")
    public ResponseEntity<AssetResponse> updateAsset(
            @PathVariable String assetId,
            @RequestBody @Valid AssetCreateRequest request) {
        AssetResponse updated = assetService.updateAsset(assetId, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAssets(@RequestBody List<String> assetIds) {
        assetService.deleteAssets(assetIds);
        return ResponseEntity.noContent().build();
    }

}
