package com.tttm.Whear.App.service;

import com.tttm.Whear.App.entity.Clothes;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.utils.request.ClothesCollectionRequest;
import com.tttm.Whear.App.utils.response.ClothesCollectionResponse;
import com.tttm.Whear.App.utils.response.CollectionResponse;
import java.util.List;

public interface ClothesCollectionService {

  public CollectionResponse addClothesToCollection(
      ClothesCollectionRequest clothesCollectionRequest) throws CustomException;

  public List<Clothes> getClothesOfCollection(Integer collectionID) throws CustomException;
}
