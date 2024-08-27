package com.tttm.Whear.App.service;

import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.utils.request.RuleMatchingClothesRequest;
import com.tttm.Whear.App.utils.response.RuleMatchingClothesResponse;

import java.util.List;

public interface RuleMatchingClothesService {
    RuleMatchingClothesRequest createNewRule(RuleMatchingClothesRequest request) throws CustomException;

    List<RuleMatchingClothesResponse> getAllRuleMatchingClothes();

    RuleMatchingClothesResponse getRuleMatchingClothesByRuleID(Integer ruleID) throws CustomException;

    RuleMatchingClothesResponse getRuleMatchingClothesByStyleAndBodyShape(Integer styleID, Integer bodyShapeID);
}
