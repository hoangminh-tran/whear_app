package com.tttm.Whear.App.service;

import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.utils.request.CommentsRequest;
import com.tttm.Whear.App.utils.response.CommentsResponse;
import java.util.List;

public interface CommentService {

  public CommentsResponse createComment(CommentsRequest commentsRequest) throws CustomException;

  public List<CommentsResponse> getAllComment(Integer postID) throws CustomException;

  void deleteComment(Integer postID, Integer commentID) throws CustomException;
}
