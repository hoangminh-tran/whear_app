package com.tttm.Whear.App.utils.response;

import java.util.List;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class ClothesResponse {

  private Integer clothesID;
  private String nameOfProduct;
  private String typeOfClothes;
  private String shape;
  private String description;
  private String link;
  private Integer rating;
  private String materials;
  private Integer reactPerClothes;
  private List<String> hashtag;
  private List<String> clothesSeasons;
  private List<String> clothesImages;
  private List<String> clothesSizes;
  private List<String> clothesColors;
  private List<String> clothesStyles;
  private List<String> clothesGender;
  private List<ReactResponse> react;
  private List<CommentsResponse> comment;
  private UserResponseStylish userResponseStylish;
  private Boolean reacted;
  private UserResponse user;
}
