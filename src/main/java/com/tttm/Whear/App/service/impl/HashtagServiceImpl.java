package com.tttm.Whear.App.service.impl;

import com.tttm.Whear.App.entity.Hashtag;
import com.tttm.Whear.App.entity.PostHashtag;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.repository.HashtagRepository;
import com.tttm.Whear.App.repository.PostHashtagRepository;
import com.tttm.Whear.App.service.HashtagService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {

  private final HashtagRepository hashtagRepository;
  private final PostHashtagRepository postHashtagRepository;

  @Override
  public Hashtag getByHashtagID(Integer hashtagID) {
    return hashtagRepository.findByHashtagID(hashtagID);
  }

  @Override
  public Hashtag createHashtag(String hashtag) throws CustomException {
    Hashtag ht = hashtagRepository.findByHashtag(hashtag);
    if (ht == null) {
      Hashtag newHashtag = Hashtag.builder()
          .hashtagID(Integer.parseInt(String.valueOf(hashtagRepository.count() + 1)))
          .hashtag(hashtag)
          .build();
     hashtagRepository.save(newHashtag);
     return newHashtag;
    } else {
      throw new CustomException("Hashtag is existed");
    }
  }

  @Override
  public Hashtag findByName(String hashtag) throws CustomException {
    Hashtag ht = hashtagRepository.findByHashtag(hashtag);
    return ht;
  }

  @Override
  public List<Hashtag> getAllHashtagOfPost(Integer postID) {

    List<Hashtag> result = hashtagRepository.findAllByPostID(postID);
//    List<PostHashtag> postHashtagList = postHashtagRepository.findAll().stream()
//        .filter(s -> s.getPostHashtagKey().getPostID() == postID).toList();
//    List<Hashtag> result = new ArrayList<>();
//    for (PostHashtag postHT : postHashtagList) {
//      if (result == null) {
//        result = new ArrayList<>();
//      }
//      result.addAll(hashtagRepository.findAll().stream()
//          .filter(hashtag -> hashtag.getHashtagID() == postHT.getPostHashtagKey().getHashtagID())
//          .toList());
//    }
    return result;
  }
}
