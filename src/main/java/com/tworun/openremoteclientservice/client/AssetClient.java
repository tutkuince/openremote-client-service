package com.tworun.openremoteclientservice.client;

import com.tworun.openremoteclientservice.dto.AssetCreateRequest;
import com.tworun.openremoteclientservice.dto.AssetResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "assetClient", url = "${openremote.api.baseurl}")
public interface AssetClient {

    @PostMapping(
            value = "/asset",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    AssetResponse createAsset(
            @RequestHeader("Authorization") String accessToken,
            @RequestBody AssetCreateRequest assetCreateRequest
    );

    @GetMapping("/asset/{assetId}")
    AssetResponse getAsset(
            @RequestHeader("Authorization") String accessToken,
            @PathVariable("assetId") String assetId
    );

    @PutMapping("/asset/{assetId}")
    AssetResponse updateAsset(
            @RequestHeader("Authorization") String token,
            @PathVariable("assetId") String assetId,
            @RequestBody AssetCreateRequest request
    );
}
