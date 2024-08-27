package com.tttm.Whear.App.entity;

import com.tttm.Whear.App.entity.common.AuditEntity;
import com.tttm.Whear.App.enums.StatusCollection;
import jakarta.persistence.*;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "collection")
@EntityListeners(AuditingEntityListener.class)
public class Collection extends AuditEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "collectionID")
  private Integer collectionID;

  @Column(name = "nameOfCollection", columnDefinition = "nvarchar(550)")
  private String nameOfCollection;

  @Column(name = "numberOfClothes")
  private Integer numberOfClothes;

  @Column(name = "typeOfCollection", columnDefinition = "nvarchar(550)")
  private String typeOfCollection;

  @Column(name = "collectionStatus")
  private StatusCollection collectionStatus;

  @Column(name = "userID", nullable = false)
  private String userID;
  @ManyToOne
  @JoinColumn(name = "userID", referencedColumnName = "userID", nullable = false, insertable = false, updatable = false)
  private User user;

  @Column(name = "imgUrl", columnDefinition="TEXT")
  private String imgUrl;
}
