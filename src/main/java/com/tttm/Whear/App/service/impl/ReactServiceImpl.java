package com.tttm.Whear.App.service.impl;

import com.tttm.Whear.App.constant.ConstantMessage;
import com.tttm.Whear.App.entity.Post;
import com.tttm.Whear.App.entity.React;
import com.tttm.Whear.App.entity.User;
import com.tttm.Whear.App.enums.TypeOfPosts;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.repository.PostRepository;
import com.tttm.Whear.App.repository.ReactRepository;
import com.tttm.Whear.App.service.HistoryService;
import com.tttm.Whear.App.service.ReactService;
import com.tttm.Whear.App.service.UserService;
import com.tttm.Whear.App.utils.request.ReactRequest;
import com.tttm.Whear.App.utils.response.ReactResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReactServiceImpl implements ReactService {

  private final ReactRepository reactRepository;
  private final UserService userService;
  private final PostRepository postService;
  private final HistoryService historyService;

  @Override
  public ReactResponse unSendReact(ReactRequest reactRequest) throws CustomException {
    if (reactRequest.getPostID() == null || reactRequest.getUserID() == null) {
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }

    if (reactRequest.getUserID().isEmpty() || reactRequest.getUserID().isBlank()) {
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }

    User user = userService.getUserEntityByUserID(reactRequest.getUserID());
    if (user == null) {
      throw new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage());
    }

    if (reactRequest.getPostID().toString().isBlank() || reactRequest.getPostID().toString()
        .isEmpty()) {
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }

    Post post = postService.getPostsByPostID(reactRequest.getPostID());
    if (post == null) {
      throw new CustomException(ConstantMessage.RESOURCE_NOT_FOUND.getMessage());
    }

    React finded = reactRepository.findReact(reactRequest.getUserID(), reactRequest.getPostID());
    // Check whether User React Posts or not
    if (finded != null) {
      reactRepository.deleteReact(reactRequest.getUserID(), reactRequest.getPostID());
      historyService.deleteHistoryItemBasedOnReactFeature(reactRequest.getUserID(),
          reactRequest.getPostID());
      return null;
    } else {
      reactRepository.insertReact(
          reactRequest.getUserID(),
          reactRequest.getPostID(),
          reactRequest.getReact().trim().toUpperCase()
      );
      if (post.getTypeOfPosts().equals(TypeOfPosts.CLOTHES)) {
        historyService.createHistoryItemBasedOnReactFeature(reactRequest.getUserID(),
            reactRequest.getPostID());
      }
    }

    return convertToReactResponse(
        reactRepository.findReact(reactRequest.getUserID(), reactRequest.getPostID())
    );
  }

  @Override
  public List<ReactResponse> getPostReact(Integer postID) throws CustomException {
    List<ReactResponse> responseList = new ArrayList<>();
    List<React> reactList = reactRepository.getPostReact(postID);
    if (reactList != null) {
      for (React r : reactList) {
        if (responseList == null) {
          responseList = new ArrayList<>();
        }
        responseList.add(convertToReactResponse(r));
      }
    }
    return responseList;
  }

  @Override
  public List<ReactResponse> getClothesReact(ReactRequest reactRequest) throws CustomException {
    List<ReactResponse> responseList = new ArrayList<>();
    List<React> reactList = reactRepository.getPostReact(reactRequest.getPostID());
    if (reactList != null) {
      for (React r : reactList) {
        if (responseList == null) {
          responseList = new ArrayList<>();
        }
        responseList.add(convertToReactResponse(r));
      }
    }
    return responseList;
  }

  @Override
  public Integer getAllReactPerClothes(Integer postID) throws CustomException {
    if (postID.toString().isBlank() || postID.toString()
        .isEmpty()) {
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }

    Post post = postService.getPostsByPostID(postID);
    if (post == null) {
      throw new CustomException(ConstantMessage.RESOURCE_NOT_FOUND.getMessage());
    }

    return reactRepository.getAllReactPerClothes(postID);
  }

  @Override
  public ReactResponse checkContain(Integer postID, String userID) throws CustomException {
    if (postID == null || userID == null) {
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }

    if (userID.isEmpty() || userID.isBlank()) {
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }

    User user = userService.getUserEntityByUserID(userID);
    if (user == null) {
      throw new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage());
    }

    if (postID.toString().isBlank() || postID.toString()
        .isEmpty()) {
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }

//    Post post = postService.getPostsByPostID(postID);
//    if (post == null) {
//      throw new CustomException(ConstantMessage.RESOURCE_NOT_FOUND.getMessage());
//    }

    React finded = reactRepository.findReact(userID, postID);
    return convertToReactResponse(finded);
  }

  private ReactResponse convertToReactResponse(React react) {
    if (react == null) {
      return null;
    }
    return ReactResponse
        .builder()
        .userID(
            react.getUserPostReactKey() != null ?
                react.getUserPostReactKey().getUserID() : null)
        .postID(
            react.getUserPostReactKey() != null ?
                react.getUserPostReactKey().getPostID() : null)
        .react(react.getReact() != null ?
            react.getReact() : null)
        .build();
  }
}
