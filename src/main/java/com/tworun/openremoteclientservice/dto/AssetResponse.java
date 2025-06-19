package com.tworun.openremoteclientservice.dto;

import lombok.Data;

import java.util.Map;

@Data
public class AssetResponse {
    private String id;
    private String name;
    private String type;
    private Map<String, AttributeValue> attributes;
}
