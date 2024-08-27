package com.tttm.Whear.App.entity;

import com.tttm.Whear.App.entity.common.AuditEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "news_image")
@EntityListeners(AuditingEntityListener.class)
public class NewsImages extends AuditEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "imgID")
  private Integer imgID;

  @Column(name = "imageUrl", columnDefinition="TEXT")
  private String imageUrl;

  @Column(name = "newsID")
  private Integer newsID;
  @ManyToOne
  @JoinColumn(name = "newsID", referencedColumnName = "newsID", nullable = false, insertable = false, updatable = false)
  private News news;

}
