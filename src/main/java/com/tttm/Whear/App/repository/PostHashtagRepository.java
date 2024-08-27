package com.tttm.Whear.App.repository;

import com.tttm.Whear.App.entity.PostHashtag;
import com.tttm.Whear.App.entity.PostHashtagKey;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostHashtagRepository extends JpaRepository<PostHashtag, PostHashtagKey> {

  @Modifying
  @Transactional
  @Query(value = "insert into post_hashtag (hashtagid, postid, create_date, last_modified_date) values (?1, ?2, current_timestamp, null)", nativeQuery = true)
  void insertPostHashtag(Integer hashtagid, Integer postid);

  @Modifying
  @Transactional
  @Query(value = "delete from post_hashtag where post_hashtag.hashtagid = ?1 and post_hashtag.postid = ?2", nativeQuery = true)
  void deletePostHashtag(Integer hashtagid, Integer postid);
}
