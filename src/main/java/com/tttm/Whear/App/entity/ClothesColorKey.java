package com.tttm.Whear.App.entity;

import com.tttm.Whear.App.enums.ColorType;
import jakarta.persistence.*;

import java.io.Serializable;
import lombok.Data;

@Data
@Embeddable
public class ClothesColorKey implements Serializable {
  @Column(name = "clothesID")
  private Integer clothesID;
  @ManyToOne
  @JoinColumn(name = "clothesID", referencedColumnName = "clothesID", nullable = false, insertable = false, updatable = false)
  private Clothes clothes;

  @Enumerated(EnumType.STRING)
  private ColorType color;
}
