package com.tttm.Whear.App.service.impl;

import com.tttm.Whear.App.constant.ConstantMessage;
import com.tttm.Whear.App.entity.Comments;
import com.tttm.Whear.App.entity.Hashtag;
import com.tttm.Whear.App.entity.Post;
import com.tttm.Whear.App.entity.PostHashtag;
import com.tttm.Whear.App.entity.React;
import com.tttm.Whear.App.entity.User;
import com.tttm.Whear.App.enums.TypeOfPosts;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.repository.CommentsRepostitory;
import com.tttm.Whear.App.repository.PostHashtagRepository;
import com.tttm.Whear.App.repository.PostRepository;
import com.tttm.Whear.App.repository.ReactRepository;
import com.tttm.Whear.App.service.HashtagService;
import com.tttm.Whear.App.service.PostImageService;
import com.tttm.Whear.App.service.PostService;
import com.tttm.Whear.App.service.UserService;
import com.tttm.Whear.App.utils.request.PostRequest;
import com.tttm.Whear.App.utils.response.CommentsResponse;
import com.tttm.Whear.App.utils.response.PostResponse;
import com.tttm.Whear.App.utils.response.ReactResponse;
import com.tttm.Whear.App.utils.response.UserResponse;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

  private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
  private final PostRepository postRepository;
  private final UserService userService;
  private final HashtagService hashtagService;
  private final PostHashtagRepository postHashtagRepository;
  private final PostImageService postImageService;
  private final CommentsRepostitory commentService;
  private final ReactRepository reactService;

  private boolean checkValidArguement(PostRequest postRequest) {
    return postRequest.getPostID() != null && postRequest.getUserID() != null &&
        !postRequest.getUserID().isEmpty() && !postRequest.getUserID().isBlank() &&
        !postRequest.getPostID().toString().isEmpty() && !postRequest.getPostID().toString()
        .isBlank();
  }

  @Override
//  @Caching(
//          evict = @CacheEvict(cacheNames = "posts", allEntries = true),
//          cacheable = @Cacheable(cacheNames = "post", key = "#postRequest.postID", condition = "#postRequest.postID > 0", unless = "#result == null")
//  )
  public PostResponse createPost(PostRequest postRequest) throws CustomException {
    if (postRequest.getUserID().isEmpty() || postRequest.getUserID().isBlank()) {
      logger.error(ConstantMessage.MISSING_ARGUMENT.getMessage());
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }
    User user = userService.getUserEntityByUserID(postRequest.getUserID());
    if (user == null) {
      logger.error(ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage());
      throw new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage());
    }

    List<Hashtag> hashtagList = new ArrayList<>();
    List<String> hashtags = postRequest.getHashtag();
    if (hashtags != null && !hashtags.isEmpty() && hashtags.size() > 0) {
      for (String ht : hashtags) {
        Hashtag finded = hashtagService.findByName(ht);
        Hashtag importedHastag = null;
        if (finded == null) {
          importedHastag = hashtagService.createHashtag(ht);
        } else {
          importedHastag = finded;
        }

        if (hashtagList == null) {
          hashtagList = new ArrayList<>();
        }
        hashtagList.add(importedHastag);
      }
    }

    Post post = Post
        .builder()
        .postID(Integer.valueOf(String.valueOf(postRepository.count() + 1)))
        .userID(postRequest.getUserID())
        .typeOfPosts(postRequest.getTypeOfPosts() != null ? postRequest.getTypeOfPosts() : null)
        .status(postRequest.getStatus())
        .content(postRequest.getContent())
        .date(LocalDateTime.now())
        .build();
    postRepository.save(post);

    List<String> postImage = postRequest.getImage();
    if (postImage != null && !postImage.isEmpty() && postImage.size() > 0) {
      for (String image : postImage) {
        postImageService.createImage(post.getPostID(), image);
      }
    }

    for (Hashtag ht : hashtagList) {
      postHashtagRepository.insertPostHashtag(ht.getHashtagID(), post.getPostID());
    }

    logger.info(ConstantMessage.CREATE_SUCCESS.getMessage());
    return convertToPostResponse(post);
  }

  @Override
//  @Cacheable(cacheNames = "post", key = "#postID", condition = "#postID > 0", unless = "#result == null")
  public PostResponse getPostByPostID(Integer postID) throws CustomException {
    if (postID == null) {
      logger.error(ConstantMessage.MISSING_ARGUMENT.getMessage());
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }
    Post post = postRepository.getReferenceById(postID);
    if (post == null) {
      logger.warn(ConstantMessage.RESOURCE_NOT_FOUND.getMessage());
      throw new CustomException(ConstantMessage.RESOURCE_NOT_FOUND.getMessage());
    }
    return convertToPostResponse(post);
  }

  @Override
//  @Cacheable(cacheNames = "posts")
  public List<PostResponse> getAllPost() throws CustomException {
    List<Post> postList = postRepository.findAll();
    List<PostResponse> responseList = new ArrayList<>();
    if (postList != null) {
      for (Post p : postList) {
        if (p.getTypeOfPosts().equals(TypeOfPosts.POSTS)) {
          if (responseList == null) {
            responseList = new ArrayList<>();
          }
          responseList.add(convertToPostResponse(p));
        }
      }
    }
    return responseList;
  }

  @Override
  public List<PostResponse> getAllPostByTypeOfPost(String typeOfPosts) throws CustomException {
    if (typeOfPosts == null || typeOfPosts.isEmpty() || typeOfPosts.isBlank()) {
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }
    List<Post> postList = postRepository.findAll();
    List<PostResponse> responseList = new ArrayList<>();
    if (postList != null) {
      for (Post p : postList) {
        if (typeOfPosts.equals(p.getTypeOfPosts().toString())) {
          if (responseList == null) {
            responseList = new ArrayList<>();
          }
          responseList.add(convertToPostResponse(p));
        }
      }
    }
    return responseList;
  }

  @Override
  public List<PostResponse> getAllPostByHashtag(String hashtag) throws CustomException {
    List<PostResponse> postList = new ArrayList<>();
    List<PostHashtag> postHashtagList = postHashtagRepository.findAll()
        .stream()
        .filter(postHashtag -> {
          return hashtagService.getByHashtagID(postHashtag.getPostHashtagKey().getHashtagID())
              .getHashtag()
              .equals(hashtag);
        })
        .toList();
    for (PostHashtag pht : postHashtagList) {
      if (postList == null) {
        postList = new ArrayList<>();
      }
      postList.add(convertToPostResponse(
          postRepository.getPostsByPostID(
              pht.getPostHashtagKey()
                  .getPostID()
          )
      ));
    }
    return postList;
  }

  @Override
  public List<PostResponse> getAllPostInRange(LocalDateTime startDate, LocalDateTime endDate) throws
      CustomException {
    List<Post> postList = postRepository.findAll()
        .stream()
        .filter(c -> c.getDate().isAfter(startDate) && c.getDate().isBefore(endDate))
        .toList();
    List<PostResponse> responseList = new ArrayList<>();
    if (postList != null && !postList.isEmpty() && postList.size() > 0) {
      for (Post p : postList) {
        if (responseList == null) {
          responseList = new ArrayList<>();
        }
        responseList.add(convertToPostResponse(p));
      }
    }
    return responseList;
  }

  @Override
//  @Caching(
//      evict = {
//          @CacheEvict(cacheNames = "posts", allEntries = true),
//          @CacheEvict(cacheNames = "post", key = "#postID")
//      }
//  )
  public Boolean deletePostByPostID(Integer postID) throws CustomException {
    if (postID == null) {
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }
    Post post = postRepository.getPostsByPostID(postID);
    if (post == null) {
      throw new CustomException(ConstantMessage.RESOURCE_NOT_FOUND.getMessage());
    }

    List<PostHashtag> postHashtagList = postHashtagRepository.findAll()
        .stream()
        .filter(postHashtag -> {
          return postRepository.getPostsByPostID(postHashtag.getPostHashtagKey().getPostID())
              != null;
        })
        .toList();
    for (PostHashtag pth : postHashtagList) {
      postHashtagRepository.deletePostHashtag(
          pth.getPostHashtagKey().getHashtagID(),
          pth.getPostHashtagKey().getPostID()
      );
    }
    postImageService.deleteByPostID(postID);
    reactService.deleteByPostID(postID);
    commentService.deleteByPostID(postID);
    try {
      return postRepository.deleteByPostID(postID) > 0;
    } catch (Exception ex) {
      throw ex;
    }
  }

  @Override
//  @Caching(
//      evict = @CacheEvict(cacheNames = "posts", allEntries = true),
//      put = @CachePut(cacheNames = "post", key = "#postRequest.postID", unless = "#result == null")
//  )
  public PostResponse updatePost(PostRequest postRequest) throws CustomException {
    if (postRequest.getPostID().toString().isBlank() || postRequest.getPostID().toString()
        .isEmpty()) {
      logger.error(ConstantMessage.RESOURCE_NOT_FOUND.getMessage());
      throw new CustomException(ConstantMessage.RESOURCE_NOT_FOUND.getMessage());
    }
    if (!checkValidArguement(postRequest)) {
      logger.error(ConstantMessage.MISSING_ARGUMENT.getMessage());
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }
    User user = userService.getUserEntityByUserID(postRequest.getUserID());
    if (user == null) {
      logger.warn(ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage());
      throw new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage());
    }
    Post post = postRepository.getPostsByPostID(postRequest.getPostID());
    if (post == null) {
      logger.warn(ConstantMessage.RESOURCE_NOT_FOUND.getMessage());
      throw new CustomException(ConstantMessage.RESOURCE_NOT_FOUND.getMessage());
    }

    List<Hashtag> hashtagList = new ArrayList<>();
    List<String> hashtags = postRequest.getHashtag();
    if (hashtags != null && !hashtags.isEmpty() && hashtags.size() > 0) {
      for (String ht : hashtags) {
        Hashtag finded = hashtagService.findByName(ht);
        Hashtag importedHastag = null;
        if (finded == null) {
          importedHastag = hashtagService.createHashtag(ht);
        } else {
          importedHastag = finded;
        }

        if (hashtagList == null) {
          hashtagList = new ArrayList<>();
        }
        hashtagList.add(importedHastag);
      }
    }

    Post updatePost = Post
        .builder()
        .postID(post.getPostID())
        .userID(post.getUserID())
        .typeOfPosts(postRequest.getTypeOfPosts() != null ? postRequest.getTypeOfPosts()
            : post.getTypeOfPosts())
        .date(postRequest.getDate() != null ? postRequest.getDate() : post.getDate())
        .status(postRequest.getStatus() != null ? postRequest.getStatus() : post.getStatus())
        .build();
    postRepository.save(updatePost);
    logger.info("Update Post Information Successfully");
    return convertToPostResponse(updatePost);
  }

  @Override
  public Post getPostEntityByPostID(Integer postID) throws CustomException {
    if (postID == null) {
      logger.error(ConstantMessage.MISSING_ARGUMENT.getMessage());
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }
    Post post = postRepository.getReferenceById(postID);
    if (post == null) {
      logger.warn(ConstantMessage.RESOURCE_NOT_FOUND.getMessage());
      throw new CustomException(ConstantMessage.RESOURCE_NOT_FOUND.getMessage());
    }
    return post;
  }

  @Override
  public List<PostResponse> getAllPostForUser(String userID) throws CustomException {
    if (userID == null || userID.isEmpty() || userID.isBlank()) {
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }
    User u = userService.getUserEntityByUserID(userID);
    if (u == null) {
      throw new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage());
    }

    List<PostResponse> responseList = new ArrayList<>();

    List<Post> postList = postRepository.getAllPostForUser(userID);
    if (postList != null && !postList.isEmpty() && postList.size() > 0) {
      for (Post p : postList) {
        if (p.getTypeOfPosts().equals(TypeOfPosts.POSTS)) {
          if (responseList == null) {
            responseList = new ArrayList<>();
          }
          PostResponse postResponse = convertToPostResponse(p);
          if (reactService.findReact(userID, p.getPostID()) != null) {
            postResponse.setReacted(true);
          } else {
            postResponse.setReacted(false);
          }
          responseList.add(postResponse);
        }
      }
    }

    return responseList;
  }

  @Override
  public List<PostResponse> getAllPostOfUser(String userID) throws CustomException {
    if (userID == null || userID.isEmpty() || userID.isBlank()) {
      throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
    }
    User u = userService.getUserEntityByUserID(userID);
    if (u == null) {
      throw new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage());
    }

    List<PostResponse> responseList = new ArrayList<>();

    List<Post> postList = postRepository.getAllByUserID(userID);
    if (postList != null && !postList.isEmpty() && postList.size() > 0) {
      for (Post p : postList) {
        if (p.getTypeOfPosts().equals(TypeOfPosts.POSTS)) {
          if (responseList == null) {
            responseList = new ArrayList<>();
          }
          PostResponse postResponse = convertToPostResponse(p);
          if (reactService.findReact(userID, p.getPostID()) != null) {
            postResponse.setReacted(true);
          } else {
            postResponse.setReacted(false);
          }
          responseList.add(postResponse);
        }
      }
    }
    return responseList;
  }

  private PostResponse convertToPostResponse(Post post) throws CustomException {
    List<String> postImage = postImageService
        .getAllImageOfPost(post.getPostID())
        .stream()
        .map(postImages -> postImages.getImageUrl().toString())
        .toList();
    List<String> postHashtag = hashtagService
        .getAllHashtagOfPost(post.getPostID())
        .stream()
        .map(postImages -> postImages.getHashtag())
        .toList();

    List<React> reactList = reactService
        .getPostReact(post.getPostID());
    List<ReactResponse> reactResponses = new ArrayList<>();
    for (React react : reactList) {
      reactResponses.add(
          ReactResponse.builder()
              .userID(
                  react.getUserPostReactKey() != null ?
                      react.getUserPostReactKey().getUserID() : null)
              .postID(
                  react.getUserPostReactKey() != null ?
                      react.getUserPostReactKey().getPostID() : null)
              .react(react.getReact() != null ?
                  react.getReact() : null)
              .build()
      );
    }

    List<Comments> commentsList = commentService
        .getAllByPostID(post.getPostID());

    List<CommentsResponse> commentsResponses = new ArrayList<>();
    for (Comments comments : commentsList) {
      UserResponse user = userService.getUserbyUserID(comments.getUserID());
      SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
      commentsResponses.add(
          CommentsResponse
              .builder()
              .commentID(comments.getCommentID())
              .content(comments.getContent())
              .postID(comments.getPostID())
              .user(user)
              .date(
                  commentService.getDateTimeByID(comments.getCommentID())
              )
              .build()
      );
    }

    return PostResponse
        .builder()
        .postID(post.getPostID())
        .userResponse(userService.getUserbyUserID(post.getUserID()))
        .typeOfPosts(post.getTypeOfPosts())
        .hashtag(postHashtag)
        .content(post.getContent())
        .image(postImage)
        .date(post.getDate().toString())
        .status(post.getStatus())
        .react(reactResponses)
        .comment(commentsResponses)
        .build();
  }
}
