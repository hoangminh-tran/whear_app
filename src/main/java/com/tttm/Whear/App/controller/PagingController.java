package com.tttm.Whear.App.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tttm.Whear.App.constant.APIConstant;
import com.tttm.Whear.App.service.PagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(APIConstant.PagingAPI.PAGING)
public class PagingController {
    private final PagingService paymentRequest;

    @PostMapping(APIConstant.PagingAPI.GET_PAGE)
    public ObjectNode getPage(
            @RequestParam Integer page,
            @RequestParam Integer pageSize,
            @RequestBody List<Object> list
    ) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode response = objectMapper.createObjectNode();
            response.put("success", 200);
            response.put("message", "Paging Successfully");
            response.set("data", objectMapper.valueToTree(paymentRequest.paging(page, pageSize, list)));
            return response;
        } catch (Exception ex) {
            ObjectNode response = objectMapper.createObjectNode();
            response.put("error", -1);
            response.put("message", ex.getMessage());
            response.set("data", null);
            return response;
        }
    }

}
