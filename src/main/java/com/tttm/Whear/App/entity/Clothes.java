package com.tttm.Whear.App.entity;

import com.tttm.Whear.App.entity.common.AuditEntity;
import com.tttm.Whear.App.enums.ClothesType;
import com.tttm.Whear.App.enums.MaterialType;
import com.tttm.Whear.App.enums.ShapeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
@Table(name = "clothes")
@EntityListeners(AuditingEntityListener.class)
public class Clothes extends AuditEntity implements Serializable {

  @Id
  @Column(name = "clothesID")
  private Integer clothesID;

  @OneToOne
  @MapsId
  @JoinColumn(name = "clothesID", referencedColumnName = "postID", nullable = false, insertable = false, updatable = false)
  private Post posts;

  @Column(name = "nameOfProduct", columnDefinition = "nvarchar(550)")
  private String nameOfProduct;

  @Column(name = "typeOfClothes")
  @Enumerated(EnumType.STRING)
  private ClothesType typeOfClothes;

  @Column(name = "shape")
  @Enumerated(EnumType.STRING)
  private ShapeType shape;

  @Column(name = "description", columnDefinition = "nvarchar(1000)")
  private String description;

  @Column(name = "link", columnDefinition="TEXT")
  private String link;

  @Column(name = "rating")
  private Integer rating;

  @Column(name = "materials")
  @Enumerated(EnumType.STRING)
  private MaterialType materials;
}
