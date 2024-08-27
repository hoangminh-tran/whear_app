package com.tttm.Whear.App.utils.response;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentsResponse implements Serializable {

  private Integer commentID;
  private UserResponse user;
  private Integer postID;
  private String content;
  private String date;
}
