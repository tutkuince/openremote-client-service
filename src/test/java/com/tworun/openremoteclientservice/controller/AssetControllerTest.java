package com.tworun.openremoteclientservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tworun.openremoteclientservice.config.AssetServiceTestConfig;
import com.tworun.openremoteclientservice.dto.AssetCreateRequest;
import com.tworun.openremoteclientservice.dto.AssetResponse;
import com.tworun.openremoteclientservice.dto.AttributeObject;
import com.tworun.openremoteclientservice.exception.AccessTokenNotFoundException;
import com.tworun.openremoteclientservice.exception.AssetNotFoundException;
import com.tworun.openremoteclientservice.exception.AuthException;
import com.tworun.openremoteclientservice.service.AssetService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {AssetServiceTestConfig.class}
)
@AutoConfigureMockMvc
class AssetControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AssetService assetService;

    private Map<String, AttributeObject> sampleAttributes;
    private AssetResponse sampleResponse;

    @BeforeEach
    void setUp() {
        sampleAttributes = new HashMap<>();
        sampleAttributes.put("brightness", new AttributeObject("brightness", 75, null, null, null));
        sampleAttributes.put("color", new AttributeObject("color", "warm_white", null, null, null));
        sampleAttributes.put("status", new AttributeObject("status", "on", null, null, null));

        sampleResponse = new AssetResponse();
        sampleResponse.setId("some-id-123");
        sampleResponse.setName("Bedroom Smart Bulb");
        sampleResponse.setType("smart_bulb");
        sampleResponse.setRealm("tutku-tenant");
        sampleResponse.setAttributes(sampleAttributes);
    }

    @Test
    @DisplayName("Should create asset and return 201 CREATED")
    void createAsset_Success() throws Exception {
        AssetCreateRequest request = new AssetCreateRequest();
        request.setName("Bedroom Smart Bulb");
        request.setType("smart_bulb");
        request.setRealm("tutku-tenant");
        request.setAttributes(sampleAttributes);

        AssetResponse response = sampleResponse;

        when(assetService.createAsset(any(AssetCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/assets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Bedroom Smart Bulb"))
                .andExpect(jsonPath("$.attributes.brightness.value").value(75));
    }

    @Test
    @DisplayName("Should fail with 400 BAD REQUEST when name is blank")
    void createAsset_MissingName() throws Exception {
        AssetCreateRequest request = new AssetCreateRequest();
        request.setName(""); // Blank
        request.setType("smart_bulb");
        request.setRealm("tutku-tenant");
        request.setAttributes(sampleAttributes);

        mockMvc.perform(post("/api/assets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", CoreMatchers.is("VALIDATION_ERROR")))
                .andExpect(jsonPath("$.details", hasItem(containsString("name"))));
    }

    @Test
    @DisplayName("Should fail with 400 BAD REQUEST when type is missing")
    void createAsset_MissingType() throws Exception {
        AssetCreateRequest request = new AssetCreateRequest();
        request.setName("Bedroom Smart Bulb");
        request.setType(""); // Blank
        request.setRealm("tutku-tenant");
        request.setAttributes(sampleAttributes);

        mockMvc.perform(post("/api/assets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.details", hasItem(containsString("type"))));
    }

    @Test
    @DisplayName("Should fail with 400 BAD REQUEST when attribute.name is blank")
    void createAsset_MissingAttributeName() throws Exception {
        Map<String, AttributeObject> attributes = sampleAttributes;
        attributes.put("temperature", new AttributeObject("", 23, null, null, null)); // Blank attribute name

        AssetCreateRequest request = new AssetCreateRequest();
        request.setName("Thermostat");
        request.setType("thermostat");
        request.setRealm("tutku-tenant");
        request.setAttributes(attributes);

        mockMvc.perform(post("/api/assets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.details", hasItem(containsString("attributes[temperature].name"))));
    }

    @Test
    @DisplayName("Should fail with 400 BAD REQUEST when realm is missing")
    void createAsset_MissingRealm() throws Exception {
        AssetCreateRequest request = new AssetCreateRequest();
        request.setName("Bedroom Smart Bulb");
        request.setType("smart_bulb");
        request.setRealm(""); // Blank
        request.setAttributes(sampleAttributes);

        mockMvc.perform(post("/api/assets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.details", hasItem(containsString("realm"))));
    }

    @Test
    @DisplayName("Should return 401 UNAUTHORIZED and error response when AuthException occurs")
    void shouldHandleAuthException() throws Exception {
        // AssetService veya başka bir servis AuthException fırlatacak şekilde mocklanır
        when(assetService.createAsset(any(AssetCreateRequest.class)))
                .thenThrow(new AuthException("Test AuthException"));

        AssetCreateRequest request = new AssetCreateRequest();
        request.setName("Bedroom Smart Bulb");
        request.setType("smart_bulb");
        request.setRealm("tutku-tenant");
        request.setAttributes(sampleAttributes);

        mockMvc.perform(post("/api/assets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("AUTH_ERROR"))
                .andExpect(jsonPath("$.message").value("Test AuthException"));
    }

    @Test
    @DisplayName("Should return 401 UNAUTHORIZED when AccessTokenNotFoundException is thrown")
    void shouldHandleAccessTokenNotFoundException() throws Exception {
        when(assetService.createAsset(any(AssetCreateRequest.class)))
                .thenThrow(new AccessTokenNotFoundException("Token not found"));

        AssetCreateRequest request = new AssetCreateRequest();
        request.setName("Bedroom Smart Bulb");
        request.setType("smart_bulb");
        request.setRealm("tutku-tenant");
        request.setAttributes(sampleAttributes);

        mockMvc.perform(post("/api/assets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("AUTH_TOKEN_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Token not found"));
    }

    @Test
    @DisplayName("Should retrieve asset by id and return 200 OK")
    void shouldRetrieveAssetById_whenExists() throws Exception {
        // Arrange
        String assetId = "some-id-123";
        Map<String, AttributeObject> attributes = new HashMap<>();
        attributes.put("brightness", new AttributeObject("brightness", 75, null, null, null));

        AssetResponse response = sampleResponse;

        when(assetService.getAsset(assetId)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/assets/{id}", assetId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(assetId))
                .andExpect(jsonPath("$.name").value("Bedroom Smart Bulb"))
                .andExpect(jsonPath("$.attributes.brightness.value").value(75));
    }

    @Test
    @DisplayName("Should return 404 NOT FOUND when asset does not exist")
    void shouldReturn404_whenAssetNotFound() throws Exception {
        // Arrange
        String missingId = "not-found-asset";
        when(assetService.getAsset(missingId))
                .thenThrow(new AssetNotFoundException("Asset not found with id: " + missingId));

        // Act & Assert
        mockMvc.perform(get("/api/assets/{id}", missingId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("ASSET_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Asset not found with id: " + missingId));
    }

    @Test
    @DisplayName("Should update asset and return 200 OK")
    void updateAsset_success() throws Exception {
        // Sample attribute map
        Map<String, AttributeObject> attributes = new HashMap<>();
        attributes.put("brightness", new AttributeObject("brightness", 90, null, null, null));
        attributes.put("color", new AttributeObject("color", "neutral_white", null, null, null));
        attributes.put("status", new AttributeObject("status", "on", null, null, null));

        // Request body
        AssetCreateRequest updateRequest = new AssetCreateRequest();
        updateRequest.setName("Updated Bulb");
        updateRequest.setType("smart_bulb");
        updateRequest.setRealm("tutku-tenant");
        updateRequest.setAttributes(attributes);

        // Mocked response
        AssetResponse updatedResponse = new AssetResponse();
        updatedResponse.setId("abc123");
        updatedResponse.setName("Updated Bulb");
        updatedResponse.setType("smart_bulb");
        updatedResponse.setRealm("tutku-tenant");
        updatedResponse.setAttributes(attributes);

        when(assetService.updateAsset(eq("abc123"), any(AssetCreateRequest.class))).thenReturn(updatedResponse);

        mockMvc.perform(put("/api/assets/abc123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("abc123"))
                .andExpect(jsonPath("$.name").value("Updated Bulb"))
                .andExpect(jsonPath("$.type").value("smart_bulb"))
                .andExpect(jsonPath("$.attributes.brightness.value").value(90))
                .andExpect(jsonPath("$.attributes.color.value").value("neutral_white"))
                .andExpect(jsonPath("$.attributes.status.value").value("on"));
    }

    @Test
    @DisplayName("Should return 404 NOT FOUND when asset does not exist")
    void updateAsset_notFound() throws Exception {
        Map<String, AttributeObject> attributes = new HashMap<>();
        attributes.put("status", new AttributeObject("status", "on", null, null, null));

        AssetCreateRequest updateRequest = new AssetCreateRequest();
        updateRequest.setName("Test");
        updateRequest.setType("smart_bulb");
        updateRequest.setRealm("tutku-tenant");
        updateRequest.setAttributes(attributes);

        when(assetService.updateAsset(eq("invalid-id"), any(AssetCreateRequest.class)))
                .thenThrow(new AssetNotFoundException("Asset not found with id: invalid-id"));

        mockMvc.perform(put("/api/assets/invalid-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("ASSET_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Asset not found with id: invalid-id"));
    }

    @Test
    @DisplayName("Should return 400 BAD REQUEST when required fields are missing")
    void updateAsset_validationError() throws Exception {
        AssetCreateRequest invalidRequest = new AssetCreateRequest();
        invalidRequest.setName("");
        invalidRequest.setType("");
        invalidRequest.setRealm("tutku-tenant");

        mockMvc.perform(put("/api/assets/abc123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.details").isArray());
    }

    @Test
    @DisplayName("Should return 204 No Content when assets are deleted successfully")
    void deleteAssets_success() throws Exception {
        List<String> ids = List.of("id1", "id2");

        mockMvc.perform(delete("/api/assets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ids)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return 404 Not Found when assets to delete do not exist")
    void deleteAssets_assetNotFound() throws Exception {
        List<String> ids = List.of("notfound-id");
        doThrow(new AssetNotFoundException("Some asset(s) not found: " + ids))
                .when(assetService).deleteAssets(ids);

        mockMvc.perform(delete("/api/assets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ids)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("ASSET_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("Some asset(s) not found: " + ids));
    }

}