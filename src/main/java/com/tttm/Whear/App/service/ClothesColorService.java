package com.tttm.Whear.App.service;

import com.tttm.Whear.App.entity.ClothesColor;
import com.tttm.Whear.App.exception.CustomException;
import java.util.List;

public interface ClothesColorService {

  void createColor(Integer clothesID, String color) throws CustomException;

  ClothesColor findByName(Integer clothesID, String color) throws CustomException;

  List<ClothesColor> getAllColorOfClothes(Integer clothesID);

  void deleteByClothesID(Integer clothesID);
}
