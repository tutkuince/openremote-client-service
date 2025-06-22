package com.tworun.openremoteclientservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Data Transfer Object for asset creation and update requests.
 * <p>
 * Used to create or update IoT assets via the OpenRemote API.
 * Includes basic validation for all required fields.
 * </p>
 */
@Data
@Schema(name = "AssetCreateRequest", description = "Request model for creating or updating an asset.")
public class AssetCreateRequest {

    @Schema(description = "Version of the asset. Must be 0 or greater.", example = "1")
    @Min(value = 0, message = "Version must be 0 or greater")
    private Long version;

    @Schema(description = "Asset name.", example = "Living Room Thermostat", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Asset name cannot be blank.")
    @Size(max = 1023, message = "Name must be at most 1023 characters.")
    private String name;

    @Schema(description = "Whether the asset is publicly readable.", example = "true")
    private Boolean accessPublicRead;

    @Schema(description = "Parent asset id (22 alphanumeric chars).", example = "1AbcD2EfG3HiJ4KlM5NoPq")
    @Pattern(regexp = "^[0-9A-Za-z]{22}$", message = "ParentId must be 22 alphanumeric characters (A-Z, a-z, 0-9).")
    private String parentId;

    @Schema(description = "Realm the asset belongs to.", example = "tutku-tenant", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Realm cannot be blank.")
    @Size(max = 255, message = "realm must be at most 255 characters.")
    private String realm;

    @Schema(description = "Type of asset (e.g., smart_bulb, thermostat).", example = "smart_bulb", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Asset type cannot be blank.")
    private String type;

    @Schema(description = "Path segments (for hierarchy).", example = "[\"Energy\",\"Living Room\"]")
    private List<String> path;

    @Schema(description = "Attributes of the asset. At least one attribute required.", example = "{\"status\":{\"name\":\"status\",\"value\":\"on\"}}", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Attributes must not be null")
    @Size(min = 1, message = "At least one attribute is required")
    private Map<String, @Valid AttributeObject> attributes;
}
