package com.tttm.Whear.App.service;

import com.tttm.Whear.App.entity.ClothesSize;
import com.tttm.Whear.App.exception.CustomException;
import java.util.List;

public interface ClothesSizeService {

  public void createSize(Integer clothesID, String size) throws CustomException;

  public ClothesSize findByName(Integer clothesID, String size) throws CustomException;

  public List<ClothesSize> getAllSizeOfClothes(Integer clothesID);

  public void deleteByClothesID(Integer clothesID);
}
