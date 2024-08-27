package com.tttm.Whear.App.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PostHashtagKey implements Serializable {

  @Column(name = "postID", insertable = false, updatable = false)
  private Integer postID;
  @ManyToOne
  @JoinColumn(name = "postID", referencedColumnName = "postID", nullable = false)
  private Post post;

  @Column(name = "hashtagID", insertable = false, updatable = false)
  private Integer hashtagID;
  @ManyToOne
  @JoinColumn(name = "hashtagID", referencedColumnName = "hashtagID", nullable = false)
  private Hashtag hashtag;
}
