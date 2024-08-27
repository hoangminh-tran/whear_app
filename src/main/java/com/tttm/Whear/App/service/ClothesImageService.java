package com.tttm.Whear.App.service;

import com.tttm.Whear.App.entity.ClothesImage;
import com.tttm.Whear.App.exception.CustomException;
import java.util.List;

public interface ClothesImageService {

  public ClothesImage getByImageID(Integer imageID);

  public ClothesImage createImage(Integer clothesID, String imageUrl) throws CustomException;

  public List<ClothesImage> getAllImageOfClothes(Integer clothesID);

  public void deleteByClothesID(Integer clothesID);
}
