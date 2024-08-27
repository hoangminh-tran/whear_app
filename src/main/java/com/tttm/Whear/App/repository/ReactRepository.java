package com.tttm.Whear.App.repository;

import com.tttm.Whear.App.entity.React;
import com.tttm.Whear.App.entity.UserPostReactKey;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactRepository extends JpaRepository<React, UserPostReactKey> {

  @Query(value = "select * from react r where r.userid = ?1 and r.postid = ?2", nativeQuery = true)
  public React findReact(String userID, Integer postID);

  @Query(value = "select * from react r where r.postid = ?1", nativeQuery = true)
  public List<React> getPostReact(Integer postID);
  @Query(value = "select COUNT(*) from react r where r.postid = ?1", nativeQuery = true)
  Integer getAllReactPerClothes(Integer postID);
  @Modifying
  @Transactional
  @Query(value = "insert into react (userid, postid, react, create_date, last_modified_date) values (?1, ?2, ?3, current_timestamp, null)", nativeQuery = true)
  void insertReact(String userID, Integer postid, String react);

  @Modifying
  @Transactional
  @Query(value = "delete from react where userid = ?1 and postid = ?2", nativeQuery = true)
  Integer deleteReact(String userID, Integer postid);

  @Modifying
  @Transactional
  @Query(value = "delete from react where postid = ?1", nativeQuery = true)
  void deleteByPostID(Integer postID);
}
