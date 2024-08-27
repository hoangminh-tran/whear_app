package com.tttm.Whear.App.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tttm.Whear.App.constant.APIConstant;
import com.tttm.Whear.App.entity.User;
import com.tttm.Whear.App.enums.ERole;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.service.BrandService;
import com.tttm.Whear.App.service.CustomerService;
import com.tttm.Whear.App.service.UserService;
import com.tttm.Whear.App.utils.request.BrandRequestDto;
import com.tttm.Whear.App.utils.request.UpdateBrandRequest;
import com.tttm.Whear.App.utils.request.UserRequest;
import com.tttm.Whear.App.utils.response.UpdateBrandResponse;
import com.tttm.Whear.App.utils.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(APIConstant.BrandAPI.BRAND)
public class BrandController {
    private final BrandService brandService;
    private final CustomerService customerService;
    private final UserService userService;

    @PostMapping(APIConstant.BrandAPI.CREATE_NEW_BRAND)
    public ObjectNode createNewBrand(@RequestBody BrandRequestDto brandRequestDto) throws CustomException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("success", 200);
            respon.put("message", "Create Brand Successfully");
            respon.set("data", objectMapper.valueToTree(brandService.createNewBrand(brandRequestDto)));
            return respon;
        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }

    @PostMapping(APIConstant.BrandAPI.UPDATE_TO_BRAND)
    public ObjectNode updateToBrand(@RequestBody UpdateBrandRequest updateBrandRequest) throws CustomException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            UpdateBrandResponse updateBrandResponse = customerService.updateToBrand(updateBrandRequest);
            UserResponse userResponse = userService.getUserbyUserID(updateBrandResponse.getBrandID());
            User user = userService.getUserEntityByUserID(updateBrandResponse.getBrandID());
            updateBrandResponse.setBaseCustomer(
                    userResponse
            );
            userService.updateStatusUser(
                    updateBrandResponse.getBrandID()
            );
            userService.updateUserByUserID(
                    UserRequest
                            .builder()
                            .userID(userResponse.getUserID())
                            .role(ERole.BRAND)
                            .email(userResponse.getEmail())
                            .phone(userResponse.getPhone())
                            .username(user.getUsername())
                            .password(user.getPassword())
                            .dateOfBirth(user.getDateOfBirth())
                            .gender(user.getGender())
                            .imgUrl(user.getImgUrl())
                            .language(user.getLanguage())
                            .build()
            );
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("success", 200);
            respon.put("message", "Create Brand Successfully");
            respon.set("data", objectMapper.valueToTree(updateBrandResponse));
            return respon;
        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }


    @GetMapping(APIConstant.BrandAPI.GET_HOT_BRAND)
    public ObjectNode getListHotBrand() throws CustomException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("success", 200);
            respon.put("message", "Get List Hot Brand Successfully");
            ArrayNode arr = objectMapper.valueToTree(brandService.getListHotBrand());
            respon.set("data", arr);
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
