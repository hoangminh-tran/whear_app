package com.tttm.Whear.App.service;

import com.tttm.Whear.App.entity.ClothesStyle;
import com.tttm.Whear.App.exception.CustomException;
import java.util.List;

public interface ClothesStyleService {

  public void createStyle(Integer clothesID, String style) throws CustomException;

  public ClothesStyle findByName(Integer clothesID, String style) throws CustomException;

  public List<ClothesStyle> getAllStyleOfClothes(Integer clothesID);

  public void deleteByClothesID(Integer clothesID);
}
