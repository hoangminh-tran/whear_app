package com.tttm.Whear.App.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tttm.Whear.App.constant.APIConstant.NotificationAPI;
import com.tttm.Whear.App.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(NotificationAPI.NOTIFICATION)
public class NotificationController {

  private final NotificationService notificationService;

  @GetMapping(NotificationAPI.GET_ALL_NOTIFICATION)
  public ObjectNode getAllNotification(@RequestParam("userid") String userid) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      ObjectNode respon = objectMapper.createObjectNode();
      respon.put("success", 200);
      respon.put("message", "Get All notification Successfully");
      respon.set("data",
          objectMapper.valueToTree(notificationService.getNotificationOfUser(userid)));
      return respon;
    } catch (Exception ex) {
      ObjectNode respon = objectMapper.createObjectNode();
      respon.put("error", -1);
      respon.put("message", ex.getMessage());
      respon.set("data", null);
      return respon;
    }
  }

  @GetMapping(NotificationAPI.GET_UNREAD_NOTIFICATION)
  public ObjectNode getUnreadNotification(@RequestParam("userid") String userid) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      ObjectNode respon = objectMapper.createObjectNode();
      respon.put("success", 200);
      respon.put("message", "Get unread notification Successfully");
      respon.set("data",
          objectMapper.valueToTree(notificationService.getUnreadNotificationOfUser(userid)));
      return respon;
    } catch (Exception ex) {
      ObjectNode respon = objectMapper.createObjectNode();
      respon.put("error", -1);
      respon.put("message", ex.getMessage());
      respon.set("data", null);
      return respon;
    }
  }

  @PutMapping(NotificationAPI.UN_READ_NOTIFICATION)
  public ObjectNode getUnreadNotification(@RequestParam("noti_id") Integer noti_id) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      ObjectNode respon = objectMapper.createObjectNode();
      respon.put("success", 200);
      respon.put("message", "Update notification Successfully");
      respon.set("data",
          objectMapper.valueToTree(notificationService.markAsUn_Read(noti_id)));
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
