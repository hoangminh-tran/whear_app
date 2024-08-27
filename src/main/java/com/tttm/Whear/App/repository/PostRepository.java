package com.tttm.Whear.App.repository;

import com.tttm.Whear.App.entity.Post;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

  public Post getPostsByPostID(Integer postID);

  public List<Post> getAllByUserID(String userID);

  @Query(value = "select * from posts p where p.userid in ( select f.following_userid from follower f where f.follower_userid = ?1 )  or p.userid = ?1 order by p.create_date desc ", nativeQuery = true)
  public List<Post> getAllPostForUser(String userID);

  @Modifying
  @Transactional
  @Query(value = "delete from posts where postid = ?1", nativeQuery = true)
  public Integer deleteByPostID(Integer postID);
}
