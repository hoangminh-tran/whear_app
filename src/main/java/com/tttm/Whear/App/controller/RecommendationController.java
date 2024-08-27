package com.tttm.Whear.App.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tttm.Whear.App.constant.APIConstant;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.service.ReactService;
import com.tttm.Whear.App.service.RecommendationService;
import com.tttm.Whear.App.utils.request.FollowRequest;
import com.tttm.Whear.App.utils.response.ClothesResponse;
import com.tttm.Whear.App.utils.response.RecommendationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(APIConstant.RecommendationAPI.RECOMMENDATION)
@RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationService recommendationService;
    private final ReactService reactService;

    @GetMapping(APIConstant.RecommendationAPI.GET_LIST_RECOMMMENDATION_BY_USER_HISTORY_ITEMS)
    public ObjectNode getListRecommendationByUserHistoryItems(@RequestParam("userID") String userID)
            throws CustomException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<ClothesResponse> clothesResponseList = recommendationService.getListRecommendationByUserHistoryItems(userID);
//            List<RecommendationResponse> responseList = new ArrayList<>();
//            for (ClothesResponse clothesResponse : clothesResponseList) {
//                responseList.add(
//                        RecommendationResponse
//                                .builder()
//                                .clothes(clothesResponse)
//                                .reacted(reactService.checkContain(clothesResponse.getClothesID(), userID) != null)
//                                .build()
//                );
//            }
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("success", 200);
            respon.put("message", "GET LIST RECOMMMENDATION BY USER HISTORY ITEMS SUCCESSFULLY");
            respon.set("data", objectMapper.valueToTree(clothesResponseList));
            return respon;
        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }

    @GetMapping(APIConstant.RecommendationAPI.GET_LIST_RECOMMENDATION_WHEN_FOLLOW_ANOTHER_USER)
    public ObjectNode getListRecommendationUserWhenUserFollowAnotherUser(@RequestBody FollowRequest followRequest)
            throws CustomException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("success", 200);
            respon.put("message", "GET LIST RECOMMMENDATION WHEN FOLLOW ANOTHER USER SUCCESSFULLY");
            respon.set("data", objectMapper.valueToTree(recommendationService.getListRecommendationUserWhenUserFollowAnotherUser(followRequest)));
            return respon;
        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }

    @GetMapping(APIConstant.RecommendationAPI.GET_LIST_RECOMMENDATION_BY_KEYWORD)
    public ObjectNode getListRecommendationByKeyword(@RequestParam("userID") String userID,
                                                     @RequestParam("keyword") String keyword)
            throws CustomException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<ClothesResponse> clothesResponseList = recommendationService.getListRecommendationByKeyword(userID, keyword);
//            List<RecommendationResponse> responseList = new ArrayList<>();
//            for (ClothesResponse clothesResponse : clothesResponseList) {
//                responseList.add(
//                        RecommendationResponse
//                                .builder()
//                                .clothes(clothesResponse)
//                                .reacted(reactService.checkContain(clothesResponse.getClothesID(), userID) != null)
//                                .build()
//                );
//            }
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("success", 200);
            respon.put("message", "GET LIST RECOMMMENDATION BY KEYWORD SUCCESSFULLY");
            respon.set("data", objectMapper.valueToTree(clothesResponseList));
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
