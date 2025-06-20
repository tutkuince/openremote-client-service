package com.tworun.openremoteclientservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

@Data
public class AssetCreateRequest {
    @NotBlank
    @Size(max = 1023)
    private String name;

    @NotBlank
    private String type;

    @Pattern(regexp = "^[0-9A-Za-z]{22}$")
    private String parentId;

    @NotBlank
    @Size(max = 255)
    private String realm;

    private Map<String, AttributeObject> attributes;
}
