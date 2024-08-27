package com.tttm.Whear.App.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class ClothesStyleKey implements Serializable {
    @Column(name = "styleID")
    private Integer styleID;

    @ManyToOne
    @JoinColumn(name = "styleID", referencedColumnName = "styleID", nullable = false, insertable = false, updatable = false)
    private Style style;

    @Column(name = "clothesID")
    private Integer clothesID;

    @ManyToOne
    @JoinColumn(name = "clothesID", referencedColumnName = "clothesID", nullable = false, insertable = false, updatable = false)
    private Clothes clothes;
}
