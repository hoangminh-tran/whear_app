package com.tttm.Whear.App.service.impl;

import com.tttm.Whear.App.entity.ClothesImage;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.repository.ClothesImageRepository;
import com.tttm.Whear.App.service.ClothesImageService;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ClothesImageServiceImpl implements ClothesImageService {

    private final ClothesImageRepository clothesImageRepository;

    @Override
    public ClothesImage getByImageID(Integer imageID) {
        return clothesImageRepository.findById(imageID).get();
    }

    @Override
    public ClothesImage createImage(Integer clothesID, String imageUrl) throws CustomException {
        return clothesImageRepository.save(
                ClothesImage.builder()
                        .imgID(clothesImageRepository.findAll().size() + 1)
                        .imageUrl(imageUrl)
                        .clothesID(clothesID)
                        .build()
        );
    }

    @Override
    public List<ClothesImage> getAllImageOfClothes(Integer clothesID) {
        return clothesImageRepository.getAllByClothesID(clothesID);
    }

    @Override
    public void deleteByClothesID(Integer clothesID) {
        clothesImageRepository.deleteByClothesID(clothesID);
    }
}
