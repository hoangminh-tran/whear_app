package com.tttm.Whear.App.utils.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuggestChoiceForPremiumUser {
    private String userID;

    private String choice;

    private String styleName;

    private String bodyShapeName;
}
