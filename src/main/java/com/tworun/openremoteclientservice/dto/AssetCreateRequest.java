package com.tworun.openremoteclientservice.dto;

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
public class AssetCreateRequest {
    @Min(value = 0, message = "Version must be 0 or greater")
    private Long version;

    @NotBlank(message = "Asset name cannot be blank.")
    @Size(max = 1023, message = "Name must be at most 1023 characters.")
    private String name;

    private Boolean accessPublicRead;

    @Pattern(regexp = "^[0-9A-Za-z]{22}$", message = "ParentId must be 22 alphanumeric characters (A-Z, a-z, 0-9).")
    private String parentId;

    @NotBlank(message = "Realm cannot be blank.")
    @Size(max = 255, message = "realm must be at most 255 characters.")
    private String realm;

    @NotBlank(message = "Asset type cannot be blank.")
    private String type;

    private List<String> path;

    @NotNull(message = "Attributes must not be null")
    @Size(min = 1, message = "At least one attribute is required")
    private Map<String, @Valid AttributeObject> attributes;
}
