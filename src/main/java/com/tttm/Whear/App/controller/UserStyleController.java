package com.tttm.Whear.App.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tttm.Whear.App.constant.APIConstant;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.service.UserStyleService;
import com.tttm.Whear.App.utils.request.StyleAndBodyShapeRequest;
import com.tttm.Whear.App.utils.request.UpdateStyleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(APIConstant.UserStyleAPI.USER_STYLE)
@RequiredArgsConstructor
public class UserStyleController {
    private final UserStyleService userStyleService;
    @PostMapping(APIConstant.UserStyleAPI.CREATE_STYLE_AND_BODY_SHAPE)
    public ObjectNode createStyleAndBodyShape(@RequestBody StyleAndBodyShapeRequest request) throws CustomException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("success", 200);
            respon.put("message", "Create User Style and Body Shape Successfully");
            userStyleService.createStyleAndBodyShape(request);
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

    @PutMapping(APIConstant.UserStyleAPI.UPDATE_STYLE_FOR_CUSTOMER)
    public ObjectNode updateStyleForCustomer(@RequestBody UpdateStyleRequest request) throws CustomException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("success", 200);
            respon.put("message", "Update Style for Customer Successfully");
            userStyleService.updateStyleForCustomer(request);
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
}
