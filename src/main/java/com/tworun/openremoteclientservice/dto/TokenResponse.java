package com.tworun.openremoteclientservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Token response DTO for handling OAuth2 access token information
 * received from the OpenRemote authorization server.
 */
@Data
public class TokenResponse {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private int expiresIn;

    @JsonProperty("refresh_expires_in")
    private int refreshExpiresIn;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("not-before-policy")
    private int notBeforePolicy;

    private String scope;
}
