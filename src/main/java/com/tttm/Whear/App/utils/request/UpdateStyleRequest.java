package com.tttm.Whear.App.utils.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.N;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateStyleRequest {
    private String userID;

    private String oldStyleName;

    private String newStyleName;
}
