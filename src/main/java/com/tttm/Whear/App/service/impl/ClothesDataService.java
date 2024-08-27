package com.tttm.Whear.App.service.impl;

import com.tttm.Whear.App.dto.Pairs;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.utils.response.ClothesResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class ClothesDataService {
    private List<Pairs> clothesItemList;
    private List<ClothesResponse> clothesResponseList;
    private final Logger logger = LoggerFactory.getLogger(ClothesDataService.class);

    public List<ClothesResponse> getClothesBaseOnTypeOfClothesAndColorOrMaterials(String typeOfClothes,
                                                                                  String color,
                                                                                  String materials,
                                                                                  String userGender) throws CustomException {
        logger.warn("Check Data {} {} {}", typeOfClothes, color, materials);
        return clothesResponseList
                .stream()
                .skip(67)
                .filter(clothesResponse -> clothesResponse != null)
                .filter(clothesResponse -> clothesResponse.getTypeOfClothes().equals(typeOfClothes))
                .filter(clothesResponse -> clothesResponse.getClothesColors().stream().anyMatch(COLOR -> COLOR.contains(color)))
                .filter(clothesResponse -> clothesResponse.getClothesGender().stream().anyMatch(GENDER -> GENDER.toUpperCase().equals(userGender.toUpperCase())))
                .collect(Collectors.toList());
    }

    public List<ClothesResponse> getClothesBaseOnTypeOfClothesAndMaterial(String typeOfClothes,
                                                                          String materials,
                                                                          String userGender) throws CustomException {
        logger.warn("Check Data {} {}", typeOfClothes, materials);
        return clothesResponseList
                .stream()
                .skip(67)
                .filter(clothesResponse -> clothesResponse != null)
                .filter(clothesResponse -> clothesResponse.getTypeOfClothes().equals(typeOfClothes))
                .filter(clothesResponse -> clothesResponse.getClothesGender().stream().anyMatch(GENDER -> GENDER.toUpperCase().equals(userGender.toUpperCase())))
                .collect(Collectors.toList());
    }
}
