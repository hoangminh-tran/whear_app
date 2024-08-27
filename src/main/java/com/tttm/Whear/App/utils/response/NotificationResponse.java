package com.tttm.Whear.App.utils.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationResponse {
  private Integer notiID;
  private UserResponse baseUserID;
  private UserResponse targetUserID;
  private String action;
  private Integer actionID;
  private String message;
  private Boolean status;
  private String dateTime;
}
