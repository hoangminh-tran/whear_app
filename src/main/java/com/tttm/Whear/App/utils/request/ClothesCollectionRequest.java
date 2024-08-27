package com.tttm.Whear.App.utils.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClothesCollectionRequest {

  private Integer collectionID;
  private Integer clothesID;
}
