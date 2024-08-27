package com.tttm.Whear.App.service;

import com.tttm.Whear.App.entity.ClothesSeason;
import com.tttm.Whear.App.exception.CustomException;
import java.util.List;

public interface ClothesSeasonService {

  void createSeason(Integer clothesID, String season) throws CustomException;

  ClothesSeason findByName(Integer clothesid, String season) throws CustomException;

  List<ClothesSeason> getAllSeasonOfClothes(Integer clothesid);

  void deleteByClothesID(Integer clothesid);
}
