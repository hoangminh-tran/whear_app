package com.tttm.Whear.App.entity;

import com.tttm.Whear.App.entity.common.AuditEntity;
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
@Table(name = "react")
@EntityListeners(AuditingEntityListener.class)
public class React extends AuditEntity implements Serializable{

  @EmbeddedId
  private UserPostReactKey userPostReactKey;

  @Column(name = "react", unique = false, nullable = true, columnDefinition = "nvarchar(550)")
  private String react;
}
