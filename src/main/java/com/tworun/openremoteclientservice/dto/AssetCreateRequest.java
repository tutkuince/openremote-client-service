package com.tworun.openremoteclientservice.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

@Data
public class AssetCreateRequest {
    @NotBlank(message = "Asset name cannot be blank.")
    @Size(max = 1023, message = "Name must be at most 1023 characters.")
    private String name;

    @NotBlank(message = "Asset type cannot be blank.")
    private String type;

    @Pattern(regexp = "^[0-9A-Za-z]{22}$", message = "ParentId must be 22 alphanumeric characters (A-Z, a-z, 0-9).")
    private String parentId;

    @NotBlank(message = "Realm cannot be blank.")
    @Size(max = 255, message = "realm must be at most 255 characters.")
    private String realm;

    @Valid
    private Map<String, AttributeObject> attributes;
}
