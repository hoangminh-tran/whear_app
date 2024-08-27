package com.tttm.Whear.App.dto;

import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PairConsineSimilarity {
    private int clothesID;
    private double consineSimilarity;
}
