package com.tttm.Whear.App.service.impl;

import com.tttm.Whear.App.entity.ClothesGender;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.repository.ClothesGenderRepository;
import com.tttm.Whear.App.service.ClothesGenderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClothesGenderServiceImpl implements ClothesGenderService {
    private final ClothesGenderRepository clothesGenderRepository;

    @Override
    public void createGender(Integer clothesID, String gender_type) throws CustomException {
        clothesGenderRepository.insertClothesGender(clothesID, gender_type);
    }

    @Override
    public ClothesGender findByName(Integer clothesID, String gender_type) throws CustomException {
        return clothesGenderRepository.findByName(clothesID, gender_type);
    }

    @Override
    public List<ClothesGender> getAllGenderOfClothes(Integer clothesID) {
        return clothesGenderRepository.getAllGenderOfClothes(clothesID);
    }

    @Override
    public void deleteByClothesID(Integer clothesID) {
        clothesGenderRepository.deleteByClothesID(clothesID);
    }
}
