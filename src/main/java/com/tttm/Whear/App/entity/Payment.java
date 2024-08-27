package com.tttm.Whear.App.entity;

import com.tttm.Whear.App.entity.common.AuditEntity;
import com.tttm.Whear.App.enums.StatusGeneral;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
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
@Table(name = "payment")
@EntityListeners(AuditingEntityListener.class)
public class Payment extends AuditEntity implements Serializable {

  @Id
  @Column(name = "paymentID")
  private Integer paymentID;

  @Column(name = "customerID")
  private String customerID;
  @ManyToOne
  @JoinColumn(name = "customerID", referencedColumnName = "customerID", nullable = false, insertable = false, updatable = false)
  private Customer customer;

  @Column(name = "value", unique = false, nullable = true)
  private Integer value;

  @Column(name = "status")
  private String status;

  @Column(name = "checkoutUrl")
  private String checkoutUrl;

  @Column(name = "qrCode")
  private String qrCode;
}
