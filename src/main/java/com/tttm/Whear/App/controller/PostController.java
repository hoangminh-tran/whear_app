package com.tttm.Whear.App.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tttm.Whear.App.constant.APIConstant.PostAPI;
import com.tttm.Whear.App.enums.ENotificationAction;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.service.FollowService;
import com.tttm.Whear.App.service.NotificationService;
import com.tttm.Whear.App.service.PostService;
import com.tttm.Whear.App.service.ReactService;
import com.tttm.Whear.App.utils.request.DateTimeRequest;
import com.tttm.Whear.App.utils.request.NotificationRequest;
import com.tttm.Whear.App.utils.request.PostRequest;
import com.tttm.Whear.App.utils.response.NotificationResponse;
import com.tttm.Whear.App.utils.response.PostResponse;
import com.tttm.Whear.App.utils.response.UserResponse;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(PostAPI.POST)
public class PostController {

  private final PostService postService;
  private final SimpMessagingTemplate messagingTemplate;
  private final NotificationService notificationService;
  private final FollowService followService;
  private final ReactService reactService;

  @PostMapping(PostAPI.CREATE_POST)
  public ObjectNode createPost(@RequestBody PostRequest postRequest) throws CustomException {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      PostResponse postResponse = postService.createPost(postRequest);
      ObjectNode respon = objectMapper.createObjectNode();
      respon.put("success", 200);
      respon.put("message", "Create new post Successfully");
      respon.set("data", objectMapper.valueToTree(postResponse));

      List<UserResponse> follwers = followService.getAllFollowerUser(postRequest.getUserID());
      if (follwers != null && !follwers.isEmpty() && follwers.size() > 0) {
        for (UserResponse user : follwers) {
          NotificationRequest notiRequest = NotificationRequest.builder()
              .action(ENotificationAction.POST.name())
              .actionID(postResponse.getPostID())
              .baseUserID(postRequest.getUserID())
              .targetUserID(user.getUserID())
              .dateTime(LocalDateTime.now())
              .message("New Post")
              .status(false)
              .build();
          NotificationResponse notiresponse = notificationService.sendNotification(notiRequest);
          notiRequest.setNotiID(notiresponse.getNotiID());
          messagingTemplate.convertAndSend("/topic/public", notiRequest);
        }
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

  @GetMapping(PostAPI.GET_POST_BY_POST_ID)
  public ObjectNode getPostByPostID(@RequestParam("post_id") Integer postID,
      @RequestParam("based_userid") String userID) throws CustomException {
    ObjectMapper objectMapper = new ObjectMapper();
    try {

      PostResponse postResponse = postService.getPostByPostID(postID);
      if (reactService.checkContain(postID, userID) != null) {
        postResponse.setReacted(true);
      } else {
        postResponse.setReacted(false);
      }

//      responseObject.put("post",
//          objectMapper.valueToTree(postResponse));
//      responseObject.put("react",
//          objectMapper.valueToTree(reactService.checkContain(postID, userID)));

      ObjectNode respon = objectMapper.createObjectNode();
      respon.put("success", 200);
      respon.put("message", "Get post Successfully");
      respon.set("data", objectMapper.valueToTree(postResponse));
      return respon;
    } catch (Exception ex) {
      ObjectNode respon = objectMapper.createObjectNode();
      respon.put("error", -1);
      respon.put("message", ex.getMessage());
      respon.set("data", null);
      return respon;
    }
  }

  @GetMapping(PostAPI.GET_ALL_POST_FOR_A_USER)
  public ObjectNode getPostForAUser(@RequestParam("user_id") String user_id)
      throws CustomException {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      ObjectNode respon = objectMapper.createObjectNode();
      respon.put("success", 200);
      respon.put("message", "Get post Successfully");
      respon.set("data", objectMapper.valueToTree(postService.getAllPostForUser(user_id)));
      return respon;
    } catch (Exception ex) {
      ObjectNode respon = objectMapper.createObjectNode();
      respon.put("error", -1);
      respon.put("message", ex.getMessage());
      respon.set("data", null);
      return respon;
    }
  }

  @GetMapping(PostAPI.GET_ALL_POST_OF_A_USER)
  public ObjectNode getPostOfAUser(@RequestParam("user_id") String user_id)
      throws CustomException {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      ObjectNode respon = objectMapper.createObjectNode();
      respon.put("success", 200);
      respon.put("message", "Get post Successfully");
      respon.set("data", objectMapper.valueToTree(postService.getAllPostOfUser(user_id)));
      return respon;
    } catch (Exception ex) {
      ObjectNode respon = objectMapper.createObjectNode();
      respon.put("error", -1);
      respon.put("message", ex.getMessage());
      respon.set("data", null);
      return respon;
    }
  }

  @GetMapping(PostAPI.GET_ALL_POST)
  public ObjectNode getAllPost() throws CustomException {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      ObjectNode respon = objectMapper.createObjectNode();
      respon.put("success", 200);
      respon.put("message", "Get all post Successfully");
      respon.set("data", objectMapper.valueToTree(postService.getAllPost()));
      return respon;
    } catch (Exception ex) {
      ObjectNode respon = objectMapper.createObjectNode();
      respon.put("error", -1);
      respon.put("message", ex.getMessage());
      respon.set("data", null);
      return respon;
    }
  }

  @GetMapping(PostAPI.GET_POST_BY_TYPE_OF_POST)
  public ObjectNode getPostByTypeOfPost(@RequestParam("type_of_post") String typeOfPosts)
      throws CustomException {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      ObjectNode respon = objectMapper.createObjectNode();
      respon.put("success", 200);
      respon.put("message", "Get post by type of post Successfully");
      respon.set("data",
          objectMapper.valueToTree(postService.getAllPostByTypeOfPost(typeOfPosts)));
      return respon;
    } catch (Exception ex) {
      ObjectNode respon = objectMapper.createObjectNode();
      respon.put("error", -1);
      respon.put("message", ex.getMessage());
      respon.set("data", null);
      return respon;
    }
  }

  @GetMapping(PostAPI.GET_POST_BY_HASHTAG)
  public ObjectNode getPostByHashtag(@RequestParam("hashtag") String hashtag)
      throws CustomException {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      ObjectNode respon = objectMapper.createObjectNode();
      respon.put("success", 200);
      respon.put("message", "Get post by hashtag Successfully");
      respon.set("data", objectMapper.valueToTree(postService.getAllPostByHashtag(hashtag)));
      return respon;
    } catch (Exception ex) {
      ObjectNode respon = objectMapper.createObjectNode();
      respon.put("error", -1);
      respon.put("message", ex.getMessage());
      respon.set("data", null);
      return respon;
    }
  }

  @GetMapping(PostAPI.GET_POST_IN_RANGE)
  public ObjectNode getPostInRange(@RequestBody DateTimeRequest dateTimeRequest) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      ObjectNode respon = objectMapper.createObjectNode();
      respon.put("success", 200);
      respon.put("message", "Get all post in range Successfully");
      respon.set("data", objectMapper.valueToTree(
          postService.getAllPostInRange(dateTimeRequest.getStartDate(),
              dateTimeRequest.getEndDate())));
      return respon;
    } catch (Exception ex) {
      ObjectNode respon = objectMapper.createObjectNode();
      respon.put("error", -1);
      respon.put("message", ex.getMessage());
      respon.set("data", null);
      return respon;
    }
  }

  @DeleteMapping(PostAPI.DELETE_POST_BY_POSTID)
  public ObjectNode deletePostByPostID(@RequestParam(name = "post_id") Integer postID) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      ObjectNode respon = objectMapper.createObjectNode();
      if (postService.deletePostByPostID(postID)) {
        respon.put("success", 200);
        respon.put("message", "Delete post Successfully");
        respon.set("data", null);
      } else {
        respon.put("fail", 0);
        respon.put("message", "Delete post Fail");
        respon.set("data", null);
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

  @PutMapping(PostAPI.UPDATE_POST)
  public ObjectNode updatePost(@RequestBody PostRequest postRequest) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      ObjectNode respon = objectMapper.createObjectNode();
      PostResponse postResponse = postService.updatePost(postRequest);
      respon.put("success", 200);
      respon.put("message", "Update post Successfully");
      respon.set("data", objectMapper.valueToTree(postResponse));
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
