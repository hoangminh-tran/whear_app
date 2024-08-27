package com.tttm.Whear.App.service.impl;

import com.tttm.Whear.App.entity.ClothesColor;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.repository.ClothesColorRepository;
import com.tttm.Whear.App.service.ClothesColorService;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ClothesColorServiceImpl implements ClothesColorService {

    private final ClothesColorRepository clothesColorRepository;

    @Override
    public void createColor(Integer clothesID, String color) throws CustomException {
        clothesColorRepository.insertClothesColor(clothesID, color);
    }

    @Override
    public ClothesColor findByName(Integer clothesID, String color) throws CustomException {
        return clothesColorRepository.findByName(clothesID, color);
    }

    @Override
    public List<ClothesColor> getAllColorOfClothes(Integer clothesID) {
        return clothesColorRepository.getAllColorOfClothes(clothesID);
    }

    @Override
    public void deleteByClothesID(Integer clothesID) {
        clothesColorRepository.deleteByClothesID(clothesID);
    }
}
