package com.tttm.Whear.App.service;

import com.tttm.Whear.App.entity.PostImages;
import com.tttm.Whear.App.exception.CustomException;
import java.util.List;

public interface PostImageService {

  public PostImages getByImageID(Integer imageID);

  public PostImages createImage(Integer postID, String imageUrl) throws CustomException;

  public List<PostImages> getAllImageOfPost(Integer postID);

  public void deleteByPostID(Integer postID);
}
