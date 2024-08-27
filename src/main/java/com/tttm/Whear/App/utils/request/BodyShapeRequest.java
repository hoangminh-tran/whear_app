package com.tttm.Whear.App.utils.request;

import com.tttm.Whear.App.enums.BodyShapeType;
import jakarta.persistence.Column;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class BodyShapeRequest {
    private String bodyShapeName;

    private double minHeight;

    private double maxHeight;

    private double minWeight;

    private double maxWeight;
}
