package com.tttm.Whear.App.entity;

import com.tttm.Whear.App.entity.common.AuditEntity;
import com.tttm.Whear.App.enums.BodyShapeType;
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
@Table(name = "body_shape")
@EntityListeners(AuditingEntityListener.class)
public class BodyShape extends AuditEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bodyShapeID")
    private Integer bodyShapeID;

    @Column(name = "bodyShapeName")
    @Enumerated(EnumType.STRING)
    private BodyShapeType bodyShapeName;

    @Column(name = "minHeight")
    private double minHeight;

    @Column(name = "maxHeight")
    private double maxHeight;

    @Column(name = "minWeight")
    private double minWeight;

    @Column(name = "maxWeight")
    private double maxWeight;
}
