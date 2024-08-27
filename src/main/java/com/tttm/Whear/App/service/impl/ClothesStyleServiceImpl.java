package com.tttm.Whear.App.service.impl;

import com.tttm.Whear.App.entity.ClothesStyle;
import com.tttm.Whear.App.entity.Style;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.repository.ClothesStyleRepository;
import com.tttm.Whear.App.repository.StyleRepository;
import com.tttm.Whear.App.service.ClothesStyleService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClothesStyleServiceImpl implements ClothesStyleService {

  private final ClothesStyleRepository clothesStyleRepository;
  private final StyleRepository styleRepository;

  @Override
  public void createStyle(Integer clothesID, String style) throws CustomException {
    Style findedStyle = styleRepository.getStyleByStyleName(style.trim().toUpperCase());
    clothesStyleRepository.insertClothesStyle(clothesID, findedStyle.getStyleID());
  }

  @Override
  public ClothesStyle findByName(Integer clothesID, String style) throws CustomException {
    Style findedStyle = styleRepository.getStyleByStyleName(style.trim().toUpperCase());
    return clothesStyleRepository.findByName(clothesID, findedStyle.getStyleID());
  }

  @Override
  public List<ClothesStyle> getAllStyleOfClothes(Integer clothesID) {
    return clothesStyleRepository.getAllStyleOfClothes(clothesID);
  }

  @Override
  public void deleteByClothesID(Integer clothesID) {
    clothesStyleRepository.deleteByClothesID(clothesID);
  }
}
