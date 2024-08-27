package com.tttm.Whear.App.utils.request;

import com.tttm.Whear.App.enums.TypeOfPosts;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostRequest {

  private Integer postID;
  private String userID;
  private TypeOfPosts typeOfPosts;
  private List<String> hashtag;
  private LocalDateTime date;
  private String status;
  private String content;
  private List<String> image;
}
