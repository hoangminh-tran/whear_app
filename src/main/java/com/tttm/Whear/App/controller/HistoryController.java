package com.tttm.Whear.App.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tttm.Whear.App.constant.APIConstant;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.service.HistoryService;
import com.tttm.Whear.App.utils.request.HistoryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(APIConstant.HistoryAPI.HISTORY)
@RequiredArgsConstructor
public class HistoryController {
    private final HistoryService historyService;

    @PostMapping(APIConstant.HistoryAPI.CREATE_HISTORY_ITEM)
    public ObjectNode createHistoryItem(@RequestBody HistoryRequest historyRequest) throws CustomException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("success", 200);
            respon.put("message", "Create History Items Successfully");
            historyService.createHistoryItemBySearching(historyRequest);
            respon.set("data", null);
            return respon;
        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }

    @GetMapping(APIConstant.HistoryAPI.GET_ALL_HISTORY_ITEMS_BY_CUSTOMER_ID)
    public ObjectNode getAllHistoryItemsByCustomerID(@RequestParam("customerID") String customerID) throws CustomException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("success", 200);
            respon.put("message", "Get All History Items By CustomerID Successfully");
            respon.set("data", objectMapper.valueToTree(historyService.getAllHistoryItemsByCustomerID(customerID)));
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
