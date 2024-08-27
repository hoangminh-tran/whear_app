package com.tttm.Whear.App.utils.request;

import com.tttm.Whear.App.enums.StatusCollection;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CollectionRequest {

  private Integer collectionID;
  private String userID;
  private String nameOfCollection;
  private String typeOfCollection;
  private Integer numberOfClothes;
  private StatusCollection collectionStatus;
  private String imgUrl;
}
