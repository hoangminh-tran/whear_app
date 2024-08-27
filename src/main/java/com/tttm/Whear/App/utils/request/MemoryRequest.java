package com.tttm.Whear.App.utils.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class MemoryRequest {
    private String styleName;

    private String bodyShapeName;

    private String topInsideID;

    private String topInsideColor;

    private String topOutsideID;

    private String topOutsideColor;

    private String bottomKindID;

    private String bottomColor;

    private String shoesTypeID;

    private String shoesTypeColor;

    private String accessoryKindID;

    private String dislikeClothesByUser;

    private String suggestClothesToUser;

    private boolean isAcceptedOldOutfit;
}
