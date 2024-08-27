package com.tttm.Whear.App.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotBrandResponse {
    private String brandID;
    private List<ClothesResponse> clothesResponseList;
    private Integer totalReactPost;
}
