package com.tttm.Whear.App.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tttm.Whear.App.constant.APIConstant;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.service.RuleMatchingClothesService;
import com.tttm.Whear.App.utils.request.RuleMatchingClothesRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(APIConstant.RuleMatchingClothesAPI.RULE_MATCHING_CLOTHES)
public class RuleMatchingClothesController {
    private final RuleMatchingClothesService ruleMatchingClothesService;

    @PostMapping(APIConstant.RuleMatchingClothesAPI.CREATE_NEW_RULE)
    public ObjectNode createNewRule(@RequestBody RuleMatchingClothesRequest request) throws CustomException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("success", 200);
            respon.put("message", "Create new Rule Matching Clothes Successfully");
            respon.set("data", objectMapper.valueToTree(ruleMatchingClothesService.createNewRule(request)));
            return respon;
        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }

    @GetMapping(APIConstant.RuleMatchingClothesAPI.GET_ALL_RULE)
    public ObjectNode getAllRuleMatchingClothes() throws CustomException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("success", 200);
            respon.put("message", "Get List Rule Matching Clothes Successfully");
            respon.set("data", objectMapper.valueToTree(ruleMatchingClothesService.getAllRuleMatchingClothes()));
            return respon;
        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }

    @GetMapping(APIConstant.RuleMatchingClothesAPI.GET_RULE_BY_ID)
    public ObjectNode getRuleMatchingClothesByRuleID(@RequestParam(name = "ruleID") Integer ruleID) throws CustomException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("success", 200);
            respon.put("message", "Get Rule Matching Clothes By ID Successfully");
            respon.set("data", objectMapper.valueToTree(ruleMatchingClothesService.getRuleMatchingClothesByRuleID(ruleID)));
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
