package com.tttm.Whear.App.service.impl;

import com.tttm.Whear.App.constant.ConstantMessage;
import com.tttm.Whear.App.entity.Follower;
import com.tttm.Whear.App.entity.Notification;
import com.tttm.Whear.App.entity.User;
import com.tttm.Whear.App.enums.ENotificationAction;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.repository.NotificationRepository;
import com.tttm.Whear.App.service.FollowService;
import com.tttm.Whear.App.service.NotificationService;
import com.tttm.Whear.App.service.PostService;
import com.tttm.Whear.App.service.UserService;
import com.tttm.Whear.App.utils.request.NotificationRequest;
import com.tttm.Whear.App.utils.response.NotificationResponse;
import com.tttm.Whear.App.utils.response.PostResponse;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

  private final NotificationRepository notificationRepository;
  private final PostService postService;
  private final UserService userService;
  private final FollowService followService;

  @Override
  public List<NotificationResponse> getNotificationOfUser(String userID) throws CustomException {
    if (userID == null || userID.isBlank() || userID.isEmpty()) {
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }
    User user = userService.getUserEntityByUserID(userID);
    if (user == null) {
      throw new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage());
    }
    List<Notification> notificationList = notificationRepository.getAllByTargetUserID(userID);
    List<NotificationResponse> responseList = new ArrayList<>();
    if (!notificationList.isEmpty() && notificationList.size() > 0) {
      for (Notification noti : notificationList) {
        if (responseList == null) {
          responseList = new ArrayList<>();
        }
        responseList.add(mapToNotificationRespons(noti));
      }
    }
    return responseList;
  }

  @Override
  public List<NotificationResponse> getUnreadNotificationOfUser(String userID)
      throws CustomException {
    if (userID == null || userID.isBlank() || userID.isEmpty()) {
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }
    User user = userService.getUserEntityByUserID(userID);
    if (user == null) {
      throw new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage());
    }
    List<Notification> notificationList = notificationRepository.getUnreadNotification(userID);
    List<NotificationResponse> responseList = new ArrayList<>();
    if (!notificationList.isEmpty() && notificationList.size() > 0) {
      for (Notification noti : notificationList) {
        if (responseList == null) {
          responseList = new ArrayList<>();
        }
        responseList.add(mapToNotificationRespons(noti));
      }
    }
    return responseList;
  }

  @Override
  public NotificationResponse sendNotification(NotificationRequest notification)
      throws CustomException {
    if (notification == null) {
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }

    if (notification.getBaseUserID() == null
        || notification.getTargetUserID() == null
        || notification.getAction() == null
        || notification.getActionID() == null
        || notification.getBaseUserID().isEmpty()
        || notification.getBaseUserID().isBlank()
        || notification.getTargetUserID().isBlank()
        || notification.getTargetUserID().isEmpty()
        || notification.getAction().isEmpty()
        || notification.getAction().isBlank()
        || notification.getActionID().toString().isBlank()
        || notification.getActionID().toString().isBlank()
    ) {
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }
    String baseUserID = notification.getBaseUserID();
    User baseUser = userService.getUserEntityByUserID(baseUserID);
    if (baseUser == null) {
      throw new CustomException(
          ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage() + baseUserID);
    }

    String targerUserID = notification.getTargetUserID();
    User targetUser = userService.getUserEntityByUserID(targerUserID);
    if (targetUser == null) {
      throw new CustomException(
          ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage() + targerUserID);
    }

    Notification noti = Notification.builder()
        .notiID(notificationRepository.findAll().size() + 1)
        .baseUserID(baseUserID)
        .targetUserID(targerUserID)
        .build();

    String action = notification.getAction();
    if (action.equals(ENotificationAction.POST.toString())) {
      Integer postID = notification.getActionID();
      PostResponse post = postService.getPostByPostID(postID);
      if (post == null) {
        throw new CustomException(
            ConstantMessage.RESOURCE_NOT_FOUND.getMessage() + " for post: " + postID);
      }
      noti.setAction(ENotificationAction.POST);
      noti.setActionID(postID);
    } else if (action.equals(ENotificationAction.FOLLOW.toString())) {
      String baseFollowerID = notification.getActionID().toString();
      Follower follower = followService.checkContain(baseFollowerID, targerUserID);
      if (follower == null) {
        throw new CustomException(
            ConstantMessage.RESOURCE_NOT_FOUND.getMessage() + " for follow: " + baseFollowerID
                + " - " + targerUserID);
      }
      noti.setAction(ENotificationAction.FOLLOW);
      noti.setActionID(Integer.parseInt(baseUserID));
    } else if (action.equals(ENotificationAction.REACT.toString())) {
      Integer postID = notification.getActionID();
      PostResponse post = postService.getPostByPostID(postID);
      if (post == null) {
        throw new CustomException(
            ConstantMessage.RESOURCE_NOT_FOUND.getMessage() + " for post: " + postID);
      }
      noti.setAction(ENotificationAction.REACT);
      noti.setActionID(postID);
    } else if (action.equals(ENotificationAction.COMMENT.toString())) {
      Integer postID = notification.getActionID();
      PostResponse post = postService.getPostByPostID(postID);
      if (post == null) {
        throw new CustomException(
                ConstantMessage.RESOURCE_NOT_FOUND.getMessage() + " for post: " + postID);
      }
      noti.setAction(ENotificationAction.COMMENT);
      noti.setActionID(postID);
    }
    noti.setStatus(false);
    noti.setDateTime(notification.getDateTime());
    noti.setMessage(notification.getMessage());
    Notification newNoti = notificationRepository.save(noti);
    if (newNoti != null) {
      return mapToNotificationRespons(noti);
    }
    return null;
  }

  @Override
  public NotificationResponse markAsUn_Read(Integer notificationID) throws CustomException {
    if (notificationID == null
        || notificationID.toString().isBlank()
        || notificationID.toString().isEmpty()) {
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }
    Notification noti = notificationRepository.getById(notificationID);
    if (noti == null) {
      throw new CustomException(
          ConstantMessage.RESOURCE_NOT_FOUND.getMessage() + " for notification: " + notificationID);
    }

    if (noti.getStatus() == false) {
      notificationRepository.un_readNotification(true, notificationID);
    } else {
      notificationRepository.un_readNotification(false, notificationID);
    }
    noti = notificationRepository.getById(notificationID);
    return mapToNotificationRespons(noti);
  }

  @Override
  public List<NotificationResponse> un_readAllNotification(String userID) throws CustomException {
    if (userID == null || userID.isBlank() || userID.isEmpty()) {
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }
    User user = userService.getUserEntityByUserID(userID);
    if (user == null) {
      throw new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage());
    }
    List<Notification> notificationList = notificationRepository.getAllByTargetUserID(userID);
    List<NotificationResponse> responseList = new ArrayList<>();
    if (!notificationList.isEmpty() && notificationList.size() > 0) {
      for (Notification noti : notificationList) {
        if (responseList == null) {
          responseList = new ArrayList<>();
        }

        notificationRepository.un_readNotification(true, noti.getNotiID());

        responseList.add(mapToNotificationRespons(noti));
      }
    }
    return responseList;
  }

  @Override
  public Boolean clearAllNotification(String userID) throws CustomException {
    if (userID == null || userID.isBlank() || userID.isEmpty()) {
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }
    User user = userService.getUserEntityByUserID(userID);
    if (user == null) {
      throw new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage());
    }
    notificationRepository.deleteNotificationByUserID(userID);
    return true;
  }

  private NotificationResponse mapToNotificationRespons(Notification notification) throws CustomException {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String formattedDateTime = notification.getDateTime().format(formatter);
    NotificationResponse response = NotificationResponse.builder()
        .notiID(notification.getNotiID())
        .baseUserID(userService.getUserbyUserID(notification.getBaseUserID()))
        .targetUserID(userService.getUserbyUserID(notification.getTargetUserID()))
        .action(notification.getAction().toString())
        .actionID(notification.getActionID())
        .status(notification.getStatus())
        .message(notification.getMessage())
        .dateTime(formattedDateTime)
        .build();
    return response;
  }
}
