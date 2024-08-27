package com.tttm.Whear.App.service;

import com.tttm.Whear.App.entity.Post;
import com.tttm.Whear.App.enums.TypeOfPosts;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.utils.request.PostRequest;
import com.tttm.Whear.App.utils.response.PostResponse;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface PostService {

  public PostResponse createPost(PostRequest post) throws CustomException;

  public PostResponse getPostByPostID(Integer postID) throws CustomException;

  public List<PostResponse> getAllPost() throws CustomException;

  public List<PostResponse> getAllPostByTypeOfPost(String typeOfPosts) throws CustomException;

  public List<PostResponse> getAllPostByHashtag(String hashtag) throws CustomException;

  public List<PostResponse> getAllPostInRange(LocalDateTime startDate, LocalDateTime endDate) throws CustomException;

  public Boolean deletePostByPostID(Integer postID) throws CustomException;

  public PostResponse updatePost(PostRequest postRequest) throws CustomException;

  public Post getPostEntityByPostID(Integer postID) throws CustomException;
  public List<PostResponse> getAllPostForUser(String userID) throws CustomException;
  public List<PostResponse> getAllPostOfUser(String userID) throws CustomException;
}
