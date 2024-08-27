package com.tttm.Whear.App.entity;

import com.tttm.Whear.App.entity.common.AuditEntity;
import com.tttm.Whear.App.enums.ENotificationAction;
import jakarta.persistence.*;

import java.time.LocalDateTime;
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
@Table(name = "notification")
@EntityListeners(AuditingEntityListener.class)
public class Notification extends AuditEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer notiID;
  private String baseUserID;
  @ManyToOne
  @JoinColumn(name = "baseUserID", referencedColumnName = "userID", insertable = false, updatable = false)
  private User baseUser;

  private String targetUserID;
  @ManyToOne
  @JoinColumn(name = "targetUserID", referencedColumnName = "userID", insertable = false, updatable = false)
  private User targetUser;
  private ENotificationAction action;
  private Integer actionID;
  private String message;
  private Boolean status;
  private LocalDateTime dateTime;
}
