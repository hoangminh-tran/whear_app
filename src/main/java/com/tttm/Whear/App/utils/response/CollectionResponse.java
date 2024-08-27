package com.tttm.Whear.App.utils.response;

import com.tttm.Whear.App.enums.StatusCollection;
import java.io.Serializable;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CollectionResponse implements Serializable {

  private Integer collectionID;
  private String userID;
  private String nameOfCollection;
  private Integer numberOfClothes;
  private String typeOfCollection;
  private StatusCollection collectionStatus;
  private String imgUrl;
  private List<ClothesResponse> clothesList;
}
