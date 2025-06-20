package com.tworun.openremoteclientservice.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class AssetResponse {
    private String id;
    private Long version;
    private String createdOn;
    private String name;
    private Boolean accessPublicRead;
    private String parentId;
    private String realm;
    private String type;
    private List<String> path;
    private Map<String, AttributeObject> attributes;
}
