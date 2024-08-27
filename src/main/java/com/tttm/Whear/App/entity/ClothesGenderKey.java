package com.tttm.Whear.App.entity;

import com.tttm.Whear.App.enums.ColorType;
import com.tttm.Whear.App.enums.GenderType;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class ClothesGenderKey implements Serializable {
    @Column(name = "clothesID")
    private Integer clothesID;
    @ManyToOne
    @JoinColumn(name = "clothesID", referencedColumnName = "clothesID", nullable = false, insertable = false, updatable = false)
    private Clothes clothes;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender_type")
    private GenderType genderType;
}
