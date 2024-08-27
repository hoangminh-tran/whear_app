package com.tttm.Whear.App.service.impl;

import com.tttm.Whear.App.entity.ClothesSeason;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.repository.ClothesSeasonRepository;
import com.tttm.Whear.App.service.ClothesSeasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClothesSeasonServiceImpl implements ClothesSeasonService {

    private final ClothesSeasonRepository clothesSeasonRepository;

    @Override
    public void createSeason(Integer clothesID, String season) throws CustomException {
        clothesSeasonRepository.insertClothesSeason(clothesID, season);
    }

    @Override
    public ClothesSeason findByName(Integer clothesid, String season) throws CustomException {
        return clothesSeasonRepository.findByName(clothesid, season);
    }

    @Override
    public List<ClothesSeason> getAllSeasonOfClothes(Integer clothesid) {
        return clothesSeasonRepository.getAllSeasonOfClothes(clothesid);
    }

    @Override
    public void deleteByClothesID(Integer clothesid) {
        clothesSeasonRepository.deleteByClothesID(clothesid);
    }
}
