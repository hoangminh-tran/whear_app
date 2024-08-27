package com.tttm.Whear.App.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tttm.Whear.App.entity.common.AuditEntity;
import com.tttm.Whear.App.enums.TokenType;
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
@Table(name = "token")
@EntityListeners(AuditingEntityListener.class)
public class Token extends AuditEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "tokenID", nullable = false, unique = true)
  private Integer tokenID;

  @Column(name = "token", nullable = false, unique = false)
  private String token;

  @Enumerated(EnumType.STRING)
  @Column(name = "tokenType", nullable = false, unique = false)
  private TokenType tokenType = TokenType.BEARER;

  @Column(name = "expired", nullable = false, unique = false)
  private boolean expired;

  @Column(name = "revoked", nullable = false, unique = false)
  private boolean revoked;

  @ManyToOne
  @JoinColumn(name = "userID", referencedColumnName = "userID")
  @JsonBackReference
  private User userToken;
}
