package com.tttm.Whear.App.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RuleMatchingClothesResponse {
    private String styleName;

    private String bodyShapeName;

    // Top Kind, Materials and Color Rule
    private String topInside;

    private String topInsideColor;

    private String topOutside;

    private String topOutsideColor;

    private String topMaterial;

    // Bottom Kind, Materials and Color Rule
    private String bottomKind;

    private String bottomColor;

    private String shoesType;

    private String shoesTypeColor;

    private String bottomMaterial;

    // Accessories Materials and Kind Rule
    private String accessoryKind;

    private String accessoryMaterial;
}
