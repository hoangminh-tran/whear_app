package com.tttm.Whear.App.constant;

import lombok.Data;

@Data
public class ConstantString {
    /* Constant String for Free User */
    public static final Integer MAXIMUM_STYLE_FOR_FREE_USER = 2;
    public static final Integer SUGGEST_CLOTHES_FOR_FREE_USER_A_DAY = 2;
    public static final Integer SUGGEST_CLOTHES_FOR_FREE_USER_A_WEEK = 4;
    public static final Integer MAXIMUM_NUMBER_TIMES_CAN_UPDATE_STYLE_FOR_FREE_USER = 4;

    /* Constant String for Premium User */
    public static final Integer SUGGEST_CLOTHES_FOR_PREMIUM_USER_A_DAY = 3;
    public static final Integer SUGGEST_CLOTHES_FOR_PREMIUM_USER_HAVING_ONE_STYLE = 21;
    public static final Integer MAXIMUM_STYLE_FOR_PREMIUM_USER_PER_WEEK = 7;
    public static final Integer PREMIUM_HAVE_ONLY_ONE_STYLE = 1;
    public static final Integer SUGGEST_CLOTHES_FOR_PREMIUM_USER_AFTER_REJECT = 1;
    public static final Integer MAXIMUM_NUMBER_TIMES_CAN_UPDATE_STYLE_FOR_PREMIUM_USER = Integer.MAX_VALUE;
    public static final String  CHANGE_TO_ANOTHER_STYLE = "CHANGE_TO_ANOTHER_STYLE";
    public static final String  SUGGEST_OLD_OUTFITS_UNTIL_NEW_OUTFITS_ARRIVE = "SUGGEST_OLD_OUTFITS_UNTIL_NEW_OUTFITS_ARRIVE";
    public static final String  SUGGEST_OUTFITS_FOR_USER = "SUGGEST_OUTFITS_FOR_USER";
    public static final String  RENEW_OUTFITS_FOR_PREMIUM = "RENEW_OUTFITS_FOR_PREMIUM";
    public static final String  SELECT_CHOICE_WHEN_RUN_OUT_OF_OUTFITS_ONLY_FOR_PREMIUM = "SELECT_CHOICE_WHEN_RUN_OUT_OF_OUTFITS_ONLY_FOR_PREMIUM";
}
