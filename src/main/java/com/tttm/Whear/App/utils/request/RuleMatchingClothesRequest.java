package com.tttm.Whear.App.utils.request;

import com.tttm.Whear.App.enums.*;
import lombok.*;
import org.checkerframework.checker.units.qual.N;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class RuleMatchingClothesRequest {
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
