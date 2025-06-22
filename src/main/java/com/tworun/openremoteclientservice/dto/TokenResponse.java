package com.tworun.openremoteclientservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Token response DTO for handling OAuth2 access token information
 * received from the OpenRemote authorization server.
 */
@Schema(name = "TokenResponse", description = "Model for authentication token response.")
@Data
public class TokenResponse {

    @Schema(description = "Access token string.", example = "eyJhbGciOiJIUzI1NiIs...")
    @JsonProperty("access_token")
    private String accessToken;

    @Schema(description = "Access token validity in seconds.", example = "300")
    @JsonProperty("expires_in")
    private int expiresIn;

    @Schema(description = "Refresh token validity in seconds.", example = "1800")
    @JsonProperty("refresh_expires_in")
    private int refreshExpiresIn;

    @Schema(description = "Token type.", example = "Bearer")
    @JsonProperty("token_type")
    private String tokenType;

    @Schema(description = "Not-before policy.", example = "0")
    @JsonProperty("not-before-policy")
    private int notBeforePolicy;

    @Schema(description = "Token scope.", example = "openid")
    private String scope;
}
