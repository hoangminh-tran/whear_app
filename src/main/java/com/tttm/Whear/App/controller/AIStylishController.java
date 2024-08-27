package com.tttm.Whear.App.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tttm.Whear.App.constant.APIConstant;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.service.AIStylishService;
import com.tttm.Whear.App.service.UserBucketService;
import com.tttm.Whear.App.utils.request.CalculateOutfitsRequest;
import com.tttm.Whear.App.utils.request.RejectClothesRequest;
import com.tttm.Whear.App.utils.request.SuggestChoiceForPremiumUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(APIConstant.AIStylishAPI.AI_STYLISH_API)
public class AIStylishController {
    private final AIStylishService stylishService;
    private final UserBucketService userBucketService;
    @GetMapping(APIConstant.AIStylishAPI.GET_SUGGEST_CLOTHES_FOR_USER)
    public ObjectNode createSuggestClothesForUSer(@RequestParam(name = "userID") String userID) throws CustomException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode respon = objectMapper.createObjectNode();
            if (userBucketService.canCallAPI(userID)) {
                Object apiData = stylishService.getSuggestClothesForUser(userID);
                userBucketService.storeCallDataAndUser(userID, apiData);
                respon.put("success", 200);
                respon.put("message", "Create Suggest Clothes for UserID: " + userID + " Successfully");
                respon.set("data", objectMapper.valueToTree(apiData));
            } else {
                Object userData = userBucketService.getOldDataByUserID(userID);
                respon.put("success", 200);
                respon.put("message", "Returning data from the first call made by UserID: " + userID + " in the current week.");
                respon.set("data", objectMapper.valueToTree(userData));
            }
            return respon;
        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }

    @GetMapping(APIConstant.AIStylishAPI.SELECT_CHOICE_WHEN_RUN_OUT_OF_OUTFITS_FOR_PREMIUM)
    public ObjectNode selectChoiceWhenRunOutOfOutfitsForPremium(@RequestBody SuggestChoiceForPremiumUser suggestChoiceForPremiumUser) throws CustomException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("success", 200);
            respon.put("message", "Select Choice When Run Out Of Outfits For Premium Successfully");
            respon.set("data", objectMapper.valueToTree(stylishService.selectChoiceWhenRunOutOfOutfitsForPremium(suggestChoiceForPremiumUser)));
            return respon;
        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }

    @PostMapping(APIConstant.AIStylishAPI.RENEW_CLOTHES_AFTER_REJECT_FOR_PREMIUM_USER)
    public ObjectNode createSuggestClothesForUSer(@RequestBody RejectClothesRequest rejectClothesRequest) throws CustomException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("success", 200);
            respon.put("message", "Create New Clothes After Reject for Premium User Successfully");
            respon.set("data", objectMapper.valueToTree(stylishService.createNewClothesAfterRejectClothesForPremiumUser(rejectClothesRequest)));
            return respon;
        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }

    @PostMapping(APIConstant.AIStylishAPI.CALCULATE_MAXIMUM_OUTFITS_CAN_GENERATE)
    public ObjectNode calculateMaximumOutfitsCanGenerate(@RequestBody CalculateOutfitsRequest calculateOutfitsRequest) throws CustomException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("success", 200);
            respon.put("message", "Calculate Maximum Outfits Successfully");
            respon.set("data", objectMapper.valueToTree(stylishService.calculateMaximumOutfitsCanGenerate(calculateOutfitsRequest)));
            return respon;
        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }
}
