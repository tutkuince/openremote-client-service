package com.tworun.openremoteclientservice.dto;

import lombok.Data;

import java.util.Map;

@Data
public class AssetCreateRequest {
    private String name;
    private String type;
    private String parentId;
    private Map<String, AttributeValue> attributes;
}
