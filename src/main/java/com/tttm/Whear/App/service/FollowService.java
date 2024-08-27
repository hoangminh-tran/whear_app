package com.tttm.Whear.App.service;

import com.tttm.Whear.App.entity.Follower;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.utils.request.FollowRequest;
import com.tttm.Whear.App.utils.response.FollowResponse;
import com.tttm.Whear.App.utils.response.UserResponse;
import java.util.List;

public interface FollowService {

  FollowResponse userFollowAnotherUser(FollowRequest followRequest) throws CustomException;

  List<UserResponse> getAllFollowerUser(String userID) throws CustomException;

  List<UserResponse> getAllFollowingUser(String userID) throws CustomException;
  List<UserResponse> getAllNotyetFollowingUser(String userID) throws CustomException;
  List<UserResponse> getAllFollowingUserExceptCurrentUser(String userID, String currentUserID) throws CustomException;
  Follower checkContain(String baseUserID, String targetUserID) throws CustomException;
  Long calculateNumberOfFollowerByUserID(String userID) throws CustomException;
  Long calculateNumberOfFollowingByUserID(String userID) throws CustomException;
}
