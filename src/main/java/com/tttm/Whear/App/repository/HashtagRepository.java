package com.tttm.Whear.App.repository;

import com.tttm.Whear.App.entity.Hashtag;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Integer> {
  Hashtag findByHashtag(String hashtag);
  Hashtag findByHashtagID(Integer hashtagID);

  @Query(value = "select * from hashtag where hashtagid in (select hashtagid from post_hashtag where postid = ?1)", nativeQuery = true)
  List<Hashtag> findAllByPostID(Integer postid);
}
