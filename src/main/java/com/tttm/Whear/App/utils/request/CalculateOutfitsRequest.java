package com.tttm.Whear.App.utils.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalculateOutfitsRequest {
    private String styleName;
    private String bodyShapeName;
    private String genderType;
}
