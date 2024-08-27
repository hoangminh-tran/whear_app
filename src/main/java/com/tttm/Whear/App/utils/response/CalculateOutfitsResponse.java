package com.tttm.Whear.App.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalculateOutfitsResponse {
    private String styleName;
    private String bodyShapeName;
    private String genderType;
    private Integer maximumOutfits;
}
