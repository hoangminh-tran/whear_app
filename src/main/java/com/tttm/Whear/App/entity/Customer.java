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
@Table(name = "customer")
@EntityListeners(AuditingEntityListener.class)
public class Customer extends AuditEntity implements Serializable {

  @Id
  @Column(name = "customerID")
  private String customerID;

  @OneToOne
  @PrimaryKeyJoinColumn(name = "customerID", referencedColumnName = "userID")
  private User user;

  @Column(name = "isFirstLogin")
  private Boolean isFirstLogin;

  @Column(name = "subRoleID")
  @PrimaryKeyJoinColumn()
  private Integer subRoleID;

  @Column(name = "numberOfUpdateStyle")
  private Integer numberOfUpdateStyle;
}
