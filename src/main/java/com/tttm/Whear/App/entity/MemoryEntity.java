package com.tttm.Whear.App.entity;

import com.tttm.Whear.App.entity.common.AuditEntity;
import com.tttm.Whear.App.enums.StatusGeneral;
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
@Table(name = "memory_entity")
@EntityListeners(AuditingEntityListener.class)
public class MemoryEntity extends AuditEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memoryID")
    private Integer memoryID;

    // Style Memory
    @Column(name = "style_name")
    private String styleName;

    // BodyShape Memory
    @Column(name = "body_shape_name")
    private String bodyShapeName;

    // Top Memory store ID in type String of Clothes Entity not Integer because it can be null
    @Column(name = "topInsideID", nullable = true)
    private String topInsideID;

    @Column(name = "topInsideColor", nullable = true)
    private String topInsideColor;

    @Column(name = "topOutsideID", nullable = true)
    private String topOutsideID;

    @Column(name = "topOutsideColor", nullable = true)
    private String topOutsideColor;

    // Bottom Memory store ID in type String of Clothes Entity not Integer because it can be null
    @Column(name = "bottomKindID", nullable = true)
    private String bottomKindID;

    @Column(name = "bottomColor", nullable = true)
    private String bottomColor;

    @Column(name = "shoesTypeID", nullable = true)
    private String shoesTypeID;

    @Column(name = "shoesTypeColor", nullable = true)
    private String shoesTypeColor;

    // Accessories store ID in type String of Clothes Entity not Integer because it can be null
    @Column(name = "accessoryKindID", nullable = true)
    private String accessoryKindID;

    // Store user
    @Column(name = "dislikeClothesByUser", nullable = true)
    private String dislikeClothesByUser;

    @Column(name = "suggestClothesToUser", nullable = true)
    private String suggestClothesToUser;

    @Column(name = "user_accepted_old_outfit", nullable = true)
    private String userAcceptedOldOutfit;
}
