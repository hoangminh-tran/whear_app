package com.tttm.Whear.App.entity;

import com.tttm.Whear.App.entity.common.AuditEntity;
import com.tttm.Whear.App.enums.StatusGeneral;
import com.tttm.Whear.App.enums.TypeOfNews;
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
@Table(name = "news")
@EntityListeners(AuditingEntityListener.class)
public class News extends AuditEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "newsID")
  private Integer newsID;

  @Column(name = "brandID")
  private String brandID;
  @ManyToOne
  @JoinColumn(name = "brandID", referencedColumnName = "brandID", nullable = false, insertable = false, updatable = false)
  private Brand brand;

  @Column(name = "title", columnDefinition = "nvarchar(550)")
  private String title;

  @Column(name = "content", columnDefinition = "TEXT")
  private String content;

  @Column(name = "typeOfNews")
  @Enumerated(EnumType.STRING)
  private TypeOfNews typeOfNews;

  @Column(name = "status")
  private StatusGeneral status;
}
