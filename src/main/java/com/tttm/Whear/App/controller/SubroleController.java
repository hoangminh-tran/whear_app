package com.tttm.Whear.App.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tttm.Whear.App.constant.APIConstant.SubroleAPI;
import com.tttm.Whear.App.entity.SubRole;
import com.tttm.Whear.App.service.SubroleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(SubroleAPI.SUBROLE)
public class SubroleController {

  private final SubroleService subroleService;

  @GetMapping(SubroleAPI.GET_SUBROLE_BY_ID)
  public ObjectNode getSubroleByID(@RequestParam("subrole_id") Integer subroleID) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      ObjectNode response = objectMapper.createObjectNode();
      SubRole subRole = subroleService.getSubroleByID(subroleID);
      if (subRole != null) {
        response.put("success", 200);
        response.put("message", "Get Successfully!");
        response.set("data", objectMapper.valueToTree(subroleService.convertToResponse(subRole)));
      }
      return response;
    } catch (Exception ex) {
      ObjectNode respon = objectMapper.createObjectNode();
      respon.put("error", -1);
      respon.put("message", ex.getMessage());
      respon.set("data", null);
      return respon;
    }
  }

  @PutMapping(SubroleAPI.UPDATE_SUBROLE)
  public ObjectNode updateSubrole(@RequestBody SubRole subRole) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      ObjectNode response = objectMapper.createObjectNode();
      SubRole newSubRole = subroleService.updateSubrole(subRole);
      if (newSubRole != null) {
        response.put("success", 200);
        response.put("message", "Get Successfully!");
        response.set("data",
            objectMapper.valueToTree(subroleService.convertToResponse(newSubRole)));
      }
      return response;
    } catch (Exception ex) {
      ObjectNode respon = objectMapper.createObjectNode();
      respon.put("error", -1);
      respon.put("message", ex.getMessage());
      respon.set("data", null);
      return respon;
    }
  }
}
