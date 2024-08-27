package com.tttm.Whear.App.service.impl;

import com.tttm.Whear.App.entity.PostImages;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.repository.PostImageRepository;
import com.tttm.Whear.App.service.PostImageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostImageServiceImpl implements PostImageService {

  private final PostImageRepository postImageRepository;

  @Override
  public PostImages getByImageID(Integer imageID) {
    return postImageRepository.findById(imageID).get();
  }

  @Override
  public PostImages createImage(Integer postID, String imageUrl) throws CustomException {
    return postImageRepository.save(
        PostImages.builder()
            .imgID(postImageRepository.findAll().size() + 1)
            .imageUrl(imageUrl)
            .postID(postID)
            .build()
    );
  }

  @Override
  public List<PostImages> getAllImageOfPost(Integer postID) {
    return postImageRepository.getAllByPostID(postID);
  }

  @Override
  public void deleteByPostID(Integer postID) {
    postImageRepository.deleteByPostID(postID);
  }
}
