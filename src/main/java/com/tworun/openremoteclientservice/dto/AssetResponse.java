package com.tworun.openremoteclientservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;


/**
 * DTO representing the response of an Asset in OpenRemote.
 * <p>
 * Returned by asset creation, update, and retrieval endpoints.
 * </p>
 */
@Schema(name = "AssetResponse", description = "Response model representing an asset.")
@Data
public class AssetResponse {
    @Schema(description = "Asset ID", example = "7clrHYLZxeyX8QMvTLijI8")
    private String id;

    @Schema(description = "Version of the asset.", example = "1")
    private Long version;

    @Schema(description = "Creation datetime (ISO8601 format).", example = "2025-06-22T15:51:28.071Z")
    private String createdOn;

    @Schema(description = "Asset name.", example = "Living Room Thermostat")
    private String name;

    @Schema(description = "Whether the asset is publicly readable.", example = "true")
    private Boolean accessPublicRead;

    @Schema(description = "Parent asset id.", example = "1AbcD2EfG3HiJ4KlM5NoPq")
    private String parentId;

    @Schema(description = "Realm the asset belongs to.", example = "tutku-tenant")
    private String realm;

    @Schema(description = "Asset type.", example = "smart_bulb")
    private String type;

    @Schema(description = "Path segments.", example = "[\"Energy\",\"Living Room\"]")
    private List<String> path;

    @Schema(description = "Attributes of the asset.")
    private Map<String, AttributeObject> attributes;
}
