package com.tttm.Whear.App.service.impl;

import com.tttm.Whear.App.constant.ConstantMessage;
import com.tttm.Whear.App.entity.Clothes;
import com.tttm.Whear.App.entity.CollectionClothes;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.repository.CollectionClothesRepository;
import com.tttm.Whear.App.service.ClothesCollectionService;
import com.tttm.Whear.App.service.ClothesService;
import com.tttm.Whear.App.service.CollectionService;
import com.tttm.Whear.App.utils.request.ClothesCollectionRequest;
import com.tttm.Whear.App.utils.request.CollectionRequest;
import com.tttm.Whear.App.utils.response.ClothesResponse;
import com.tttm.Whear.App.utils.response.CollectionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ClothesCollectionServiceImpl implements ClothesCollectionService {

    private final CollectionClothesRepository collectionClothesRepository;
    private final ClothesService clothesService;
    private final CollectionService collectionService;

    @Override
    public CollectionResponse addClothesToCollection(
            ClothesCollectionRequest clothesCollectionRequest) throws CustomException {

        if (clothesCollectionRequest == null
                || clothesCollectionRequest.getClothesID() == null
                || clothesCollectionRequest.getCollectionID() == null) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }

        if (clothesCollectionRequest.getCollectionID().toString().isEmpty()
                || clothesCollectionRequest.getCollectionID().toString().isBlank()) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }

        if (clothesCollectionRequest.getClothesID().toString().isEmpty()
                || clothesCollectionRequest.getClothesID().toString().isBlank()) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }

        ClothesResponse clothes = clothesService.getClothesByID(
                clothesCollectionRequest.getClothesID());
        if (clothes == null) {
            throw new CustomException(ConstantMessage.RESOURCE_NOT_FOUND.getMessage());
        }

        CollectionResponse collection = collectionService.getCollectionByCollectionID(
                clothesCollectionRequest.getCollectionID());
        if (collection == null) {
            throw new CustomException(ConstantMessage.RESOURCE_NOT_FOUND.getMessage());
        }


        CollectionClothes collectionClothes = collectionClothesRepository.checkContain(
                clothesCollectionRequest.getClothesID(),
                clothesCollectionRequest.getCollectionID());
        if (collectionClothes == null) {
            collectionClothesRepository.insertClothesToCollection(clothesCollectionRequest.getClothesID(),
                    clothesCollectionRequest.getCollectionID());
        } else {
            collectionClothesRepository.deleteClothesToCollection(clothesCollectionRequest.getClothesID(),
                    clothesCollectionRequest.getCollectionID());
            return null;
        }
        collectionClothes = collectionClothesRepository.checkContain(
                clothesCollectionRequest.getClothesID(),
                clothesCollectionRequest.getCollectionID());
        return mapToResponse(collectionClothes);

    }

    @Override
    public List<Clothes> getClothesOfCollection(Integer collectionID) throws CustomException {
        List<CollectionClothes> responseList = collectionClothesRepository.getAllByCollectionID(
                collectionID);
        List<Clothes> clothesList = new ArrayList<>();
        if (responseList != null && !responseList.isEmpty() && responseList.size() > 0) {
            for (CollectionClothes response : responseList) {
                if (clothesList == null) {
                    clothesList = new ArrayList<>();
                }
                clothesList.add(
                        clothesService.getClothesEntityByID(response.getCollectionClothesKey().getClothesID())
                );
            }
        }
        return clothesList;
    }

    private CollectionResponse mapToResponse(CollectionClothes collectionClothes)
            throws CustomException {
        CollectionResponse collectionResponse = collectionService.getCollectionByCollectionID(
                collectionClothes.getCollectionClothesKey().getCollectionID());

        collectionResponse = collectionService.updateCollectionByID(
                CollectionRequest
                        .builder()
                        .collectionID(collectionResponse.getCollectionID())
                        .nameOfCollection(collectionResponse.getNameOfCollection())
                        .collectionStatus(collectionResponse.getCollectionStatus())
                        .typeOfCollection(collectionResponse.getTypeOfCollection())
                        .imgUrl(collectionResponse.getImgUrl())
                        .numberOfClothes(collectionResponse.getNumberOfClothes() + 1)
                        .build()
        );

        collectionResponse = collectionService.getCollectionByCollectionID(
                collectionClothes.getCollectionClothesKey().getCollectionID());

        return collectionResponse;
    }
}
