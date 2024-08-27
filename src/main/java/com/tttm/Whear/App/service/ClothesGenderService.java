package com.tttm.Whear.App.service;

import com.tttm.Whear.App.entity.ClothesColor;
import com.tttm.Whear.App.entity.ClothesGender;
import com.tttm.Whear.App.exception.CustomException;

import java.util.List;

public interface ClothesGenderService {
    void createGender(Integer clothesID, String gender_type) throws CustomException;

    ClothesGender findByName(Integer clothesID, String gender_type) throws CustomException;

    List<ClothesGender> getAllGenderOfClothes(Integer clothesID);

    void deleteByClothesID(Integer clothesID);
}
