package com.tttm.Whear.App.dto;

import com.tttm.Whear.App.enums.*;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ClothesItemDto {
    private Integer clothesID;
    private String nameOfProduct;
    private ClothesType typeOfClothes;
    private ShapeType shape;
    private MaterialType materials;
    private List<SeasonType> seasons;
    private List<SizeType> sizes;
    private List<ColorType> colors;
    private List<StyleType> styles;
    public String sizeToString()
    {
        return this.sizes
                .stream()
                .map(size -> size + " ")
                .collect(Collectors.joining());
    }

    public String colorToString()
    {
        return this.colors
                .stream()
                .map(color -> color + " ")
                .collect(Collectors.joining());
    }

    public String seasonToString()
    {
        return this.seasons
            .stream()
            .map(season -> season + " ")
            .collect(Collectors.joining());
    }

    public String styleToString()
    {
        return this.styles
                .stream()
                .map(style -> style + " ")
                .collect(Collectors.joining());
    }
}
