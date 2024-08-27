package com.tttm.Whear.App.utils.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReactRequest {

  private String userID;
  private Integer postID;
  private String react;
}
