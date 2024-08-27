package com.tttm.Whear.App.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tttm.Whear.App.constant.APIConstant.ReactAPI;
import com.tttm.Whear.App.entity.Post;
import com.tttm.Whear.App.enums.ENotificationAction;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.service.NotificationService;
import com.tttm.Whear.App.service.PostService;
import com.tttm.Whear.App.service.ReactService;
import com.tttm.Whear.App.utils.request.NotificationRequest;
import com.tttm.Whear.App.utils.request.ReactRequest;
import com.tttm.Whear.App.utils.response.NotificationResponse;
import com.tttm.Whear.App.utils.response.PostResponse;
import com.tttm.Whear.App.utils.response.ReactResponse;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ReactAPI.REACT)
@RequiredArgsConstructor
public class ReactController {

  private final ReactService reactService;
  private final PostService postService;
  private final NotificationService notificationService;
  private final SimpMessagingTemplate messagingTemplate;

  @PostMapping(ReactAPI.UN_SEND_REACT)
  public ObjectNode un_sendReact(@RequestBody ReactRequest reactRequest) throws CustomException {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      ReactResponse reactResponse = reactService.unSendReact(reactRequest);
      ObjectNode respon = objectMapper.createObjectNode();
      respon.put("success", 200);
      respon.put("message", "UN_SEND_REACT Successfully");
      respon.set("data", objectMapper.valueToTree(reactResponse));

      PostResponse postResponse = postService.getPostByPostID(reactRequest.getPostID());
      Post post = postService.getPostEntityByPostID(postResponse.getPostID());

      if (postResponse != null) {
        NotificationRequest notiRequest = NotificationRequest.builder()
            .action(ENotificationAction.REACT.name())
            .actionID(postResponse.getPostID())
            .baseUserID(reactRequest.getUserID())
            .targetUserID(post.getUserID())
            .dateTime(LocalDateTime.now())
            .message("New React")
            .status(false)
            .build();
        NotificationResponse notiresponse = notificationService.sendNotification(notiRequest);
        notiRequest.setNotiID(notiresponse.getNotiID());
        messagingTemplate.convertAndSend("/topic/public", notiRequest);
      }
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
