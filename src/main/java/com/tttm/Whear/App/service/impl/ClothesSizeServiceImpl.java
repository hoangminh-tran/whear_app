package com.tttm.Whear.App.service.impl;

import com.tttm.Whear.App.entity.ClothesSize;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.repository.ClothesSizeRepository;
import com.tttm.Whear.App.service.ClothesSizeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ClothesSizeServiceImpl implements ClothesSizeService {

  private final ClothesSizeRepository clothesSizeRepository;

  @Override
  public void createSize(Integer clothesID, String size) throws CustomException {
    clothesSizeRepository.insertClothesSize(clothesID, size.toUpperCase());
  }

  @Override
  public ClothesSize findByName(Integer clothesID, String size) throws CustomException {
    return clothesSizeRepository.findByName(clothesID, size);
  }

  @Override
  public List<ClothesSize> getAllSizeOfClothes(Integer clothesID) {
    return clothesSizeRepository.getAllSizeOfClothes(clothesID);
  }

  @Override
  public void deleteByClothesID(Integer clothesID) {
    clothesSizeRepository.deleteByClothesID(clothesID);
  }
}
