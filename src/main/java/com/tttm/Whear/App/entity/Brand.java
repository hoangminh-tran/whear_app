package com.tttm.Whear.App.entity;

import com.tttm.Whear.App.entity.common.AuditEntity;
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
@Table(name = "brand")
@EntityListeners(AuditingEntityListener.class)
public class Brand extends AuditEntity implements Serializable {
    @Id
    @Column(name = "brandID")
    private String brandID;

    @Column(name = "brandName")
    private String brandName;

    @OneToOne
    @MapsId
    @JoinColumn(name = "brandID", referencedColumnName = "customerID", nullable = false, insertable = false, updatable = false)
    private Customer customer;

    @Column(name = "description", columnDefinition = "nvarchar(550)")
    private String description;

    @Column(name = "address", columnDefinition = "nvarchar(550)")
    private String address;

    @Column(name = "link")
    private String link;
}
