package com.tttm.Whear.App.utils.request;

import com.tttm.Whear.App.entity.ClothesColor;
import com.tttm.Whear.App.entity.ClothesImage;
import com.tttm.Whear.App.entity.ClothesSize;
import com.tttm.Whear.App.enums.SeasonType;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ClothesRequest {

  private String userID;

  private Integer clothesID;
  private String nameOfProduct;
  private String typeOfClothes;
  private String shape;
  private String description;
  private String link;
  private Integer rating;
  private String materials;

  private List<String> clothesSeasons;
  private List<String> hashtag;
  private List<String> clothesColors;
  private List<String> clothesImages;
  private List<String> clothesSizes;
  private List<String> clothesStyle;
  private List<String> clothesGender;
}
