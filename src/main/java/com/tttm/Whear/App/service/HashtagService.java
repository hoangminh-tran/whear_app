package com.tttm.Whear.App.service;

import com.tttm.Whear.App.entity.Hashtag;
import com.tttm.Whear.App.exception.CustomException;
import java.util.List;

public interface HashtagService {

  public Hashtag getByHashtagID(Integer hashtagID);

  public Hashtag createHashtag(String hashtag) throws CustomException;

  public Hashtag findByName(String hashtag) throws CustomException;

  public List<Hashtag> getAllHashtagOfPost(Integer postID);
}
