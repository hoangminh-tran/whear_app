package com.tttm.Whear.App.entity;

import com.tttm.Whear.App.entity.common.AuditEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "history")
public class History extends AuditEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "historyID")
  private Integer historyID;

  @Column(name = "customerID")
  private String customerID;
  @ManyToOne
  @JoinColumn(name = "customerID", referencedColumnName = "customerID", nullable = false, insertable = false, updatable = false)
  private Customer customer;

  @Column(name = "historyItem", unique = false, nullable = true, columnDefinition = "nvarchar(550)")
  private String historyItem;

  @Column(name = "historyIndex")
  private String historyIndex; // 1: Styles (Default, Update), 2: History Search, 3: React, Like + clothes_id
}
