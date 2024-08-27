package com.tttm.Whear.App.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tttm.Whear.App.constant.APIConstant;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.service.GenerateDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(APIConstant.GenerateDataAPI.GENERATE_DATA)
@RequiredArgsConstructor
public class GenerateDataController {
    private final GenerateDataService generateDataService;

    @GetMapping(APIConstant.GenerateDataAPI.GENERATE_RANDOM_HISTORY_USER_SEARCH)
    public ObjectNode generateRandomHistoryUserSearch(@RequestParam("userID") String userID,
                                                      @RequestParam("size") Integer size)
            throws CustomException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("success", 200);
            respon.put("message", "GENERATE RANDOM HISTORY USER SEARCH SUCCESSFULLY");
            respon.set("data", objectMapper.valueToTree(generateDataService.generateRandomHistoryUserSearch(userID, size)));
            return respon;
        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }

    @GetMapping(APIConstant.GenerateDataAPI.GENERATE_RANDOM_LIST_CLOTHES)
    public ObjectNode generateRandomListClothes(@RequestParam("userID") String userID,
                                                @RequestParam("size") Integer size)
            throws CustomException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("success", 200);
            respon.put("message", "GENERATE RANDOM LIST CLOTHES SUCCESSFULLY");
            respon.set("data", objectMapper.valueToTree(generateDataService.generateRandomListClothes(userID, size)));
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
