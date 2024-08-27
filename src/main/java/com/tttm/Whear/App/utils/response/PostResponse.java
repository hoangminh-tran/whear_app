package com.tttm.Whear.App.utils.response;

import com.tttm.Whear.App.enums.TypeOfPosts;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponse {

  private Integer postID;
  private UserResponse userResponse;
  private TypeOfPosts typeOfPosts;
  private String content;
  private List<String> hashtag;
  private List<String> image;
  private String date;
  private String status;
  private List<ReactResponse> react;
  private List<CommentsResponse> comment;
  private Boolean reacted;
}
