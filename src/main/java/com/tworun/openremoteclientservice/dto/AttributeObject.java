package com.tworun.openremoteclientservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class AttributeObject {

    @NotBlank(message = "Attribute name cannot be blank.")
    @Pattern(regexp = "^\\w+$", message = "Name must contain only letters, digits, and underscores.")
    private String name;
    private Object value;
    private Object meta;
    private Object type;
    private Long timestamp;
}
