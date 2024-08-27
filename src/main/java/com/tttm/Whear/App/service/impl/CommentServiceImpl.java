package com.tttm.Whear.App.service.impl;

import com.tttm.Whear.App.constant.ConstantMessage;
import com.tttm.Whear.App.entity.Clothes;
import com.tttm.Whear.App.entity.Comments;
import com.tttm.Whear.App.entity.Post;
import com.tttm.Whear.App.entity.User;
import com.tttm.Whear.App.enums.TypeOfPosts;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.repository.ClothesRepository;
import com.tttm.Whear.App.repository.CommentsRepostitory;
import com.tttm.Whear.App.repository.PostRepository;
import com.tttm.Whear.App.repository.UserRepository;
import com.tttm.Whear.App.service.CommentService;
import com.tttm.Whear.App.utils.request.CommentsRequest;
import com.tttm.Whear.App.utils.response.CommentsResponse;
import com.tttm.Whear.App.utils.response.UserResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

  private final UserRepository userRepository;
  private final PostRepository postRepository;
  private final ClothesRepository clothesRepository;
  private final CommentsRepostitory commentsRepostitory;

  @Override
  public CommentsResponse createComment(CommentsRequest commentsRequest) throws CustomException {
    if (commentsRequest == null) {
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }
    String userID = commentsRequest.getUserID();
    if (userID == null || userID.isEmpty() || userID.isBlank()) {
      throw new CustomException(ConstantMessage.USERID_IS_EMPTY_OR_NOT_EXIST.getMessage());
    }
    User user = userRepository.getUserByUserID(userID);
    if (user == null) {
      throw new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage());
    }

    Integer postID = commentsRequest.getPostID();
    if (postID == null || postID.toString().isBlank() || postID.toString().isEmpty()) {
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }

    Post post = postRepository.getPostsByPostID(postID);
    if (post == null) {
      throw new CustomException(ConstantMessage.RESOURCE_NOT_FOUND.getMessage());
    }

    if (post.getTypeOfPosts().equals(TypeOfPosts.CLOTHES)) {
      Clothes clothes = clothesRepository.getClothesByClothesID(postID);
      if (clothes == null) {
        throw new CustomException(ConstantMessage.RESOURCE_NOT_FOUND.getMessage());
      }
    }

    String content = commentsRequest.getContent();
    if (content == null || content.isEmpty() || content.isBlank()) {
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }

    Comments comments = Comments
        .builder()
        .commentID(commentsRepostitory.findAll().size() + 1)
        .userID(userID)
        .postID(postID)
        .content(content)
        .build();

    commentsRepostitory.save(comments);
    return convertToResponse(comments);
  }

  @Override
  public List<CommentsResponse> getAllComment(Integer postID) throws CustomException {
    if (postID == null || postID.toString().isBlank() || postID.toString().isEmpty()) {
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }
    List<Comments> commentsList = commentsRepostitory.getAllByPostID(postID);
    List<CommentsResponse> commentsResponses = new ArrayList<>();
    if (commentsList != null && !commentsList.isEmpty() && commentsList.size() > 0) {
      for (Comments comments : commentsList) {
        commentsResponses.add(convertToResponse(comments));
      }
    }
    return commentsResponses;
  }

  @Override
  public void deleteComment(Integer postID, Integer commentID) throws CustomException {
    commentsRepostitory.deleteComment(postID, commentID);
  }

  private CommentsResponse convertToResponse(Comments comments) {
    User user = userRepository.getUserByUserID(comments.getUserID());
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    CommentsResponse commentsResponse = CommentsResponse
        .builder()
        .commentID(comments.getCommentID())
        .content(comments.getContent())
        .postID(comments.getPostID())
        .user(
            UserResponse
                .builder()
                .userID(user.getUserID())
                .username(user.getNameOfUser())
                .password(user.getPassword())
                .dateOfBirth(String.valueOf(df.format(user.getDateOfBirth())))
                .phone(user.getPhone())
                .email(user.getEmail())
                .gender(user.getGender())
                .role(user.getRole())
                .imgUrl(user.getImgUrl())
                .status(user.getStatus())
                .language(user.getLanguage())
                .build()
        )
        .date(
            commentsRepostitory.getDateTimeByID(comments.getCommentID())
        )
        .build();
    return commentsResponse;
  }
}
