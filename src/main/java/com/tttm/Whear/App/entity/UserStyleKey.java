package com.tttm.Whear.App.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserStyleKey implements Serializable {
    @Column(name = "userID")
    private String userID;

    @ManyToOne
    @JoinColumn(name = "userID", referencedColumnName = "userID", nullable = false, insertable = false, updatable = false)
    private User user;

    @Column(name = "styleID")
    private Integer styleID;

    @ManyToOne
    @JoinColumn(name = "styleID", referencedColumnName = "styleID", nullable = false, insertable = false, updatable = false)
    private Style style;

    @Column(name = "style_position")
    private Integer stylePosition;
}
