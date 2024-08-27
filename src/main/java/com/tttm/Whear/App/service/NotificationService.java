package com.tttm.Whear.App.service;

import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.utils.request.NotificationRequest;
import com.tttm.Whear.App.utils.response.NotificationResponse;
import java.util.List;

public interface NotificationService {

  /**
   * GET ALL NOTIFY OF AN USER
   *
   * @param userID
   * @return
   * @throws CustomException
   */
  public List<NotificationResponse> getNotificationOfUser(String userID) throws CustomException;

  /**
   * GET ALL UNREAD NOTIFICATION OF AN USER
   *
   * @param userID
   * @return
   * @throws CustomException
   */
  public List<NotificationResponse> getUnreadNotificationOfUser(String userID)
      throws CustomException;

  /**
   * SEND NEW NOTIFICATION
   * @param notification
   * @return
   * @throws CustomException
   */
  public NotificationResponse sendNotification(NotificationRequest notification) throws CustomException;

  /**
   * UN_READ NOTIFICATION
   * @param notificationID
   * @return
   * @throws CustomException
   */
  public NotificationResponse markAsUn_Read(Integer notificationID) throws CustomException;

  /**
   * UN_READ ALL NOTIFICATION
   * @return
   * @throws CustomException
   */
  public List<NotificationResponse> un_readAllNotification(String userID) throws CustomException;

  /**
   * CLEAR ALL NOTIFICATION
   * @param userID
   * @return
   * @throws CustomException
   */
  public Boolean clearAllNotification(String userID) throws CustomException;
}
