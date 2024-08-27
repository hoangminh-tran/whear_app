package com.tttm.Whear.App.repository;

import com.tttm.Whear.App.entity.Comments;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentsRepostitory extends JpaRepository<Comments, Integer> {

  List<Comments> getAllByPostID(Integer postID);
  @Query(value = "select create_date from comments where commentid = ?1", nativeQuery = true)
  String getDateTimeByID(Integer commentID);

  @Modifying
  @Transactional
  @Query(value = "delete from comments where postid = ?1", nativeQuery = true)
  void deleteByPostID(Integer postID);

  @Modifying
  @Transactional
  @Query(value = "delete from comments where postid = ?1 and commentid=?2", nativeQuery = true)
  void deleteComment(Integer postID, Integer commentID);
}
