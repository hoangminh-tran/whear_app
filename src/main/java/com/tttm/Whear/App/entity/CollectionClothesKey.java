package com.tttm.Whear.App.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;
import lombok.Data;

@Data
@Embeddable
public class CollectionClothesKey implements Serializable {
    @Column(name = "clothesID")
    private Integer clothesID;

    @ManyToOne
    @JoinColumn(name = "clothesID", referencedColumnName = "clothesID", nullable = false, insertable = false, updatable = false)
    private Clothes clothes;

    @Column(name = "collectionID")
    private Integer collectionID;

    @ManyToOne
    @JoinColumn(name = "collectionID", referencedColumnName = "collectionID", nullable = false, insertable = false, updatable = false)
    private Collection collection;
}