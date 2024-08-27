package com.tttm.Whear.App.service.impl;

import com.tttm.Whear.App.constant.ConstantMessage;
import com.tttm.Whear.App.entity.Follower;
import com.tttm.Whear.App.entity.FollowerKey;
import com.tttm.Whear.App.entity.User;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.repository.FollowerRepository;
import com.tttm.Whear.App.service.FollowService;
import com.tttm.Whear.App.service.UserService;
import com.tttm.Whear.App.utils.request.FollowRequest;
import com.tttm.Whear.App.utils.response.FollowResponse;
import com.tttm.Whear.App.utils.response.UserResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FollowServiceImpl implements FollowService {

  private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

  private final UserService userService;

  private final FollowerRepository followerRepository;


  @Override
//  @CacheEvict(cacheNames = {"follower", "following"}, allEntries = true)
  public FollowResponse userFollowAnotherUser(FollowRequest followRequest) throws CustomException {
    if (followRequest.getBaseUserID() == null
        || followRequest.getBaseUserID().isBlank()
        || followRequest.getBaseUserID().isEmpty()

        || followRequest.getTargetUserID() == null
        || followRequest.getTargetUserID().isBlank()
        || followRequest.getTargetUserID().isEmpty()
    ) {
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }

    User firstUser = userService.getUserEntityByUserID(followRequest.getBaseUserID());
    if (firstUser == null) {
      throw new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage() + ": "
          + followRequest.getBaseUserID());
    }

    User secondUser = userService.getUserEntityByUserID(followRequest.getTargetUserID());
    if (secondUser == null) {
      throw new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage() + ": "
          + followRequest.getTargetUserID());
    }

    FollowerKey followerKey = FollowerKey
        .builder()
        .followerUser(firstUser)
        .followingUser(secondUser)
        .build();

    if (followRequest.getBaseUserID().equals(followRequest.getTargetUserID())) {
      throw new CustomException("Failed to create FollowerKey. Because Two Username are the same");
    }

    if (followerRepository.findFollowerByFollowerIdAndFollowingId(firstUser.getUserID(),
        secondUser.getUserID()) != null) {
      followerRepository.deleteFollowerByFollowerIDandFollowingID(firstUser.getUserID(),
          secondUser.getUserID());
      return new FollowResponse();
    }
    followerRepository.insertFollower(firstUser.getUserID(),
        secondUser.getUserID());
    return new FollowResponse(userService.convertToUserResponse(firstUser),
        userService.convertToUserResponse(secondUser));
  }

  @Override
//  @Cacheable(cacheNames = "follower", key = "#username", condition = "#username != null", unless = "#result == null")
  public List<UserResponse> getAllFollowerUser(String userid)
      throws CustomException // List All User Follower the User
  {
    if (userid == null || userid.isBlank() || userid.isEmpty()) {
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }

    User user = userService.getUserEntityByUserID(userid);
    if (user == null) {
      throw new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage());
    }

    List<UserResponse> userResponseList = new ArrayList<>();
    List<Follower> fl = followerRepository.findAllFollowingUserByUserID(user.getUserID());
    if (fl != null && !fl.isEmpty() && fl.size() > 0) {
      for (Follower f : fl) {
        if (userResponseList == null) {
          userResponseList = new ArrayList<>();
        }
        userResponseList.add(userService.convertToUserResponse(
            userService.getUserEntityByUserID(
                f.getFollowerKey()
                    .getFollowerUser()
                    .getUserID()
            )
        ));
      }
    }
    return userResponseList;
  }

  @Override
//  @Cacheable(cacheNames = "following", key = "#username", condition = "#username != null", unless = "#result == null")
  public List<UserResponse> getAllFollowingUser(String userid)
      throws CustomException // List All User, User Following
  {
    if (userid == null || userid.isBlank() || userid.isEmpty()) {
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }

    User user = userService.getUserEntityByUserID(userid);
    if (user == null) {
      throw new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage());
    }

    List<UserResponse> userResponseList = followerRepository.findAllFollowerUserByUserID(
            user.getUserID())
        .stream()
        .map(follower -> {
              try {
                return userService.convertToUserResponse(
                    userService.getUserEntityByUserID(
                        follower.getFollowerKey().getFollowingUser().getUserID()
                    )
                );
              } catch (CustomException e) {
                throw new RuntimeException(e);
              }
            }
        )
        .toList();
    return userResponseList;
  }

  @Override
  public List<UserResponse> getAllNotyetFollowingUser(String userID) throws CustomException {
    if (userID == null || userID.isBlank() || userID.isEmpty()) {
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }

    User user = userService.getUserEntityByUserID(userID);
    if (user == null) {
      throw new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage());
    }

    List<UserResponse> userResponseList = followerRepository.findAllNotyetFollowingUserByUserID(
                    user.getUserID())
            .stream()
            .map(follower -> {
                      try {
                        return userService.convertToUserResponse(
                                userService.getUserEntityByUserID(
                                        follower
                                )
                        );
                      } catch (CustomException e) {
                        throw new RuntimeException(e);
                      }
                    }
            )
            .toList();
    return userResponseList;
  }

  @Override
//  @Cacheable(cacheNames = "following", key = "#username", condition = "#username != null", unless = "#result == null")
  public List<UserResponse> getAllFollowingUserExceptCurrentUser(String userid, String currentUserID) throws CustomException // List All User, User Following
  {
    if (userid == null || userid.isBlank() || userid.isEmpty()) {
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }

    User user = userService.getUserEntityByUserID(userid);
    if (user == null) {
      throw new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage());
    }

    List<UserResponse> userResponseList = followerRepository.findAllFollowerUserExceptCurrentUserByUserID(
                    userid, currentUserID)
            .stream()
            .map(follower -> {
                      try {
                        return userService.convertToUserResponse(
                                userService.getUserEntityByUserID(
                                        follower.getFollowerKey().getFollowingUser().getUserID()
                                )
                        );
                      } catch (CustomException e) {
                        throw new RuntimeException(e);
                      }
                    }
            )
            .toList();
    return userResponseList;
  }
  @Override
  public Long calculateNumberOfFollowerByUserID(String userID) throws CustomException {
    if (userID == null || userID.isBlank() || userID.isEmpty()) {
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }

    User user = userService.getUserEntityByUserID(userID);
    if (user == null) {
      throw new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage());
    }

    return followerRepository.findAllFollowingUserByUserID(userID).stream().count();
  }

  @Override
  public Long calculateNumberOfFollowingByUserID(String userID) throws CustomException {
    if (userID == null || userID.isBlank() || userID.isEmpty()) {
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }

    User user = userService.getUserEntityByUserID(userID);
    if (user == null) {
      throw new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage());
    }

    return followerRepository.findAllFollowerUserByUserID(userID).stream().count();
  }

  @Override
  public Follower checkContain(String baseUserID, String targetUserID)
      throws CustomException {
    if (baseUserID == null || baseUserID.isBlank() || baseUserID.isEmpty()) {
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }

    User baseUser = userService.getUserEntityByUserID(baseUserID);
    if (baseUser == null) {
      throw new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage());
    }

    if (targetUserID == null || targetUserID.isBlank() || targetUserID.isEmpty()) {
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }

    User targetUser = userService.getUserEntityByUserID(targetUserID);
    if (targetUser == null) {
      throw new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage());
    }
    return followerRepository.findFollowerByFollowerIdAndFollowingId(baseUserID, targetUserID);
  }

  private CustomException handleInvalidUsername(String username) {
    logger.error(ConstantMessage.USERID_IS_EMPTY_OR_NOT_EXIST.getMessage());
    return new CustomException(ConstantMessage.USERID_IS_EMPTY_OR_NOT_EXIST.getMessage());
  }

  private CustomException handleUserNotFound(String username) {
    logger.error(ConstantMessage.CANNOT_FIND_USER_BY_USERNAME.getMessage());
    return new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERNAME.getMessage());
  }
}
