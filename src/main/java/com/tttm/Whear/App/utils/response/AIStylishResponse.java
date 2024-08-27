package com.tttm.Whear.App.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AIStylishResponse {
    private String styleName;
    private String bodyShapeName;
    private List<List<ClothesResponse>> outfits;
    private String message;
}
