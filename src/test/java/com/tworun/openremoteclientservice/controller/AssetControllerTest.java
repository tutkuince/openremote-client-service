package com.tworun.openremoteclientservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tworun.openremoteclientservice.config.AssetServiceTestConfig;
import com.tworun.openremoteclientservice.dto.AssetCreateRequest;
import com.tworun.openremoteclientservice.dto.AssetResponse;
import com.tworun.openremoteclientservice.dto.AttributeObject;
import com.tworun.openremoteclientservice.exception.AccessTokenNotFoundException;
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
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @BeforeEach
    void setUp() {
        sampleAttributes = new HashMap<>();
        sampleAttributes.put("brightness", new AttributeObject("brightness", 75, null, null, null));
        sampleAttributes.put("color", new AttributeObject("color", "warm_white", null, null, null));
        sampleAttributes.put("status", new AttributeObject("status", "on", null, null, null));
    }

    @Test
    @DisplayName("Should create asset and return 201 CREATED")
    void createAsset_Success() throws Exception {
        AssetCreateRequest request = new AssetCreateRequest();
        request.setName("Bedroom Smart Bulb");
        request.setType("smart_bulb");
        request.setRealm("tutku-tenant");
        request.setAttributes(sampleAttributes);

        AssetResponse response = new AssetResponse();
        response.setId("some-id-123");
        response.setName("Bedroom Smart Bulb");
        response.setType("smart_bulb");
        response.setRealm("tutku-tenant");
        response.setAttributes(sampleAttributes);

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


}