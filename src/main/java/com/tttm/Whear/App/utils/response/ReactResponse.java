package com.tttm.Whear.App.utils.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReactResponse {

  private String userID;
  private Integer postID;
  private String react;
}
