package com.tttm.Whear.App.utils.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RejectClothesRequest {
    private String userID;

    private String styleName;

    private String bodyShapeName;

    private String topInsideID;

    private String topOutsideID;

    private String bottomKindID;

    private String shoesTypeID;

    private String accessoryKindID;
}
