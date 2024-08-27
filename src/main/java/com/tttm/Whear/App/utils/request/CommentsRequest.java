package com.tttm.Whear.App.utils.request;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentsRequest implements Serializable {

  private Integer commentID;
  private String userID;
  private Integer postID;
  private String content;
}
