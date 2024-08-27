package com.tttm.Whear.App.repository;

import com.tttm.Whear.App.entity.PostImages;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostImageRepository extends JpaRepository<PostImages, Integer> {

  public List<PostImages> getAllByPostID(Integer postID);

  @Modifying
  @Transactional
  @Query(value = "delete from post_image where postid = ?1", nativeQuery = true)
  void deleteByPostID(Integer postID);
}
