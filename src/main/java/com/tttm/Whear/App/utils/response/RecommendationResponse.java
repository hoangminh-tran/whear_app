package com.tttm.Whear.App.utils.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class RecommendationResponse implements Serializable {
    ClothesResponse clothes;
    Boolean reacted;
}
