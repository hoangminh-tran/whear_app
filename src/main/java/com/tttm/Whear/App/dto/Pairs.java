package com.tttm.Whear.App.dto;

import jakarta.annotation.security.DenyAll;
import lombok.*;

import java.util.Objects;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Pairs {
   private int clothesID;
   private String ClothesItem;
}
