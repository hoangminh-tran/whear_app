package com.tttm.Whear.App.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tttm.Whear.App.constant.ConstantMessage;
import com.tttm.Whear.App.entity.Brand;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.repository.BrandRepository;
import com.tttm.Whear.App.service.BrandService;
import com.tttm.Whear.App.service.ClothesService;
import com.tttm.Whear.App.service.ReactService;
import com.tttm.Whear.App.utils.request.BrandRequestDto;
import com.tttm.Whear.App.utils.response.BrandResponse;
import com.tttm.Whear.App.utils.response.ClothesResponse;
import com.tttm.Whear.App.utils.response.HotBrandResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final ReactService reactService;
    private final ClothesService clothesService;
    private final BrandRepository brandRepository;
    private final Logger logger = LoggerFactory.getLogger(BrandServiceImpl.class);

    @Override
    public BrandResponse createNewBrand(BrandRequestDto brandRequestDto) throws CustomException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        if (brandRequestDto.getCustomerID().isEmpty() || brandRequestDto.getCustomerID().isBlank()) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }

        Brand brand = Brand.builder().brandID(brandRequestDto.getCustomerID()).brandName(brandRequestDto.getBrandName()).address(brandRequestDto.getAddress()).description(brandRequestDto.getDescription()).link(brandRequestDto.getLink()).build();

        brandRepository.createNewBrand(
                brandRequestDto.getCustomerID(),
                brand.getBrandName(),
                brand.getDescription(),
                brand.getLink(),
                brand.getAddress()
        );
        return convertToBrandResponse(brand);
    }

    @Override
    public BrandResponse getBrandByID(Integer brandID) throws CustomException {
        return convertToBrandResponse(brandRepository.getBrandByBrandID(brandID.toString()));
    }

    @Override
    public List<HotBrandResponse> getListHotBrand() throws CustomException {
        return brandRepository.getAllBrand().stream().map(brand -> {
            List<ClothesResponse> clothes = null;
            try {
                clothes = clothesService.getAllClothesByBrandID(brand.getBrandID()).stream().map(clothesResponse -> {
                    int reactPerClothes = 0;
                    try {
                        reactPerClothes = reactService.getAllReactPerClothes(clothesResponse.getClothesID());
                    } catch (CustomException e) {
                        throw new RuntimeException(e);
                    }
                    clothesResponse.setReactPerClothes(reactPerClothes);
                    return clothesResponse;
                }).sorted(Comparator.comparingInt(ClothesResponse::getReactPerClothes).reversed()).collect(Collectors.toList());
            } catch (CustomException e) {
                throw new RuntimeException(e);
            }

            int totalReactPerBrand = clothes.stream().mapToInt(ClothesResponse::getReactPerClothes).sum();

            clothes.forEach(clothesResponse -> logger.info("ClothesID: " + clothesResponse.getClothesID() + " Total Of React Per Clothes: " + clothesResponse.getReactPerClothes()));

            return HotBrandResponse.builder().brandID(brand.getBrandID()).clothesResponseList(clothes).totalReactPost(totalReactPerBrand).build();
        }).sorted(Comparator.comparingInt(HotBrandResponse::getTotalReactPost).reversed()).peek(hotBrandResponse -> logger.info("Brand ID: " + hotBrandResponse.getBrandID() + "  Total of React Per Brand: " + hotBrandResponse.getTotalReactPost())).collect(Collectors.toList());
    }

    private BrandResponse convertToBrandResponse(Brand brand) {
        return BrandResponse
                .builder()
                .brandName(brand.getBrandName())
                .brandID(brand.getBrandID())
                .address(brand.getAddress())
                .description(brand.getDescription())
                .link(brand.getLink())
                .build();
    }
}