package com.tworun.openremoteclientservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing an attribute of an asset in OpenRemote.
 * Used for both requests and responses to describe asset-specific properties.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AttributeObject", description = "Model representing an asset attribute.")
public class AttributeObject {


    @Schema(description = "Attribute name (letters, digits, underscores).", example = "status", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Attribute name cannot be blank.")
    @Pattern(regexp = "^\\w+$", message = "Name must contain only letters, digits, and underscores.")
    private String name;

    @Schema(description = "Attribute value. Can be of any type.", example = "on")
    private Object value;

    @Schema(description = "Meta information for the attribute.")
    private Object meta;

    @Schema(description = "Type information for the attribute.")
    private Object type;

    @Schema(description = "Timestamp of attribute update (epoch millis).", example = "1729688512000")
    private Long timestamp;
}
