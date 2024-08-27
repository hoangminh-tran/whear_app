package com.tttm.Whear.App.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FollowerKey implements Serializable {
  @Column(name = "followerUserID", nullable = false)
  private String followerUserID;
  @ManyToOne
  @JoinColumn(name = "followerUserID", referencedColumnName = "userID", nullable = false, insertable = false, updatable = false)
  private User followerUser;

  @Column(name = "followingUserID", nullable = false)
  private String followingUserID;
  @ManyToOne
  @JoinColumn(name = "followingUserID", referencedColumnName = "userID", nullable = false, insertable = false, updatable = false)
  private User followingUser;
}