package com.tttm.Whear.App.service.impl;

import com.tttm.Whear.App.constant.ConstantMessage;
import com.tttm.Whear.App.entity.BodyShape;
import com.tttm.Whear.App.entity.MemoryEntity;
import com.tttm.Whear.App.entity.Style;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.repository.MemoryEntityRepository;
import com.tttm.Whear.App.service.BodyShapeService;
import com.tttm.Whear.App.service.MemoryEntityService;
import com.tttm.Whear.App.service.StyleService;
import com.tttm.Whear.App.utils.request.MemoryRequest;
import com.tttm.Whear.App.utils.request.RejectClothesRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemoryEntityServiceImpl implements MemoryEntityService {
    private final MemoryEntityRepository memoryEntityRepository;
    private final BodyShapeService bodyShapeService;
    private final StyleService styleService;
    private final Logger logger = LoggerFactory.getLogger(MemoryEntityServiceImpl.class);

    @Override
    public void createMemoryEntity(MemoryRequest memoryRequest) throws CustomException {
        memoryEntityRepository.save(
                MemoryEntity
                        .builder()
                        .styleName(memoryRequest.getStyleName())
                        .bodyShapeName(memoryRequest.getBodyShapeName())
                        .topInsideID(memoryRequest.getTopInsideID())
                        .topInsideColor(memoryRequest.getTopInsideColor())
                        .topOutsideColor(memoryRequest.getTopOutsideColor())
                        .topOutsideID(memoryRequest.getTopOutsideID())
                        .bottomKindID(memoryRequest.getBottomKindID())
                        .bottomColor(memoryRequest.getBottomColor())
                        .shoesTypeID(memoryRequest.getShoesTypeID())
                        .shoesTypeColor(memoryRequest.getShoesTypeColor())
                        .accessoryKindID(memoryRequest.getAccessoryKindID())
                        .dislikeClothesByUser(memoryRequest.getDislikeClothesByUser())
                        .suggestClothesToUser(memoryRequest.getSuggestClothesToUser())
                        .build()
        );
    }

    @Override
    public MemoryEntity getMemoryByMemoryRequest(MemoryRequest memoryRequest) throws CustomException {
        return memoryEntityRepository.getMemoryByMemoryRequest(
                memoryRequest.getStyleName(),
                memoryRequest.getBodyShapeName(),
                memoryRequest.getTopInsideID(),
                memoryRequest.getTopInsideColor(),
                memoryRequest.getTopOutsideID(),
                memoryRequest.getTopOutsideColor(),
                memoryRequest.getBottomKindID(),
                memoryRequest.getBottomColor(),
                memoryRequest.getShoesTypeID(),
                memoryRequest.getShoesTypeColor(),
                memoryRequest.getAccessoryKindID()
        );
    }

    @Override
    public void updateMemoryEntityForDislikeAndSuggest(Integer memoryID, String userID, String keyword) throws CustomException {
        MemoryEntity memoryEntity = memoryEntityRepository.findById(memoryID)
                .orElseThrow(() -> new CustomException(ConstantMessage.ID_IS_EMPTY_OR_NOT_EXIST.getMessage()));

        switch (keyword) {
            case "DISLIKE":
                memoryEntityRepository.updateMemoryEntityForDislikeClothes((memoryEntity.getDislikeClothesByUser() == null  ? "," : memoryEntity.getDislikeClothesByUser()) + userID, memoryID);
                break;
            case "SUGGEST":
                memoryEntityRepository.updateMemoryEntityForSuggestClothes(memoryEntity.getSuggestClothesToUser() + userID, memoryID);
                break;
            default:
                throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }
    }

    @Override
    public Integer countMemoryByStyleAndBodyShape(String styleName, String bodyShapeName) throws CustomException {
        return memoryEntityRepository.countMemoryByStyleAndBodyShape(styleName, bodyShapeName);
    }

    @Override
    public MemoryEntity getMemoryForRejectClothesRequest(RejectClothesRequest rejectClothesRequest) throws CustomException {
        Style style = styleService.getStyleByStyleName(rejectClothesRequest.getStyleName());
        if(style == null)
        {
            throw new CustomException(ConstantMessage.STYLE_NAME_IS_NOT_EXISTED.getMessage());
        }
        BodyShape bodyShape = bodyShapeService.getBodyShapeByBodyShapeName(rejectClothesRequest.getBodyShapeName());
        if(bodyShape == null)
        {
            throw new CustomException(ConstantMessage.BODY_SHAPE_NAME_IS_NOT_EXISTED.getMessage());
        }
        return memoryEntityRepository.getMemoryForRejectClothesRequest(
                rejectClothesRequest.getStyleName(),
                rejectClothesRequest.getBodyShapeName(),
                rejectClothesRequest.getTopInsideID(),
                rejectClothesRequest.getTopOutsideID(),
                rejectClothesRequest.getBottomKindID(),
                rejectClothesRequest.getShoesTypeID(),
                rejectClothesRequest.getAccessoryKindID()
        );
    }

    @Override
    public Integer countNumberOfOutfitsBaseOnStyleBodyShapeUserID(String styleName, String bodyShapeName, String userID) throws CustomException {
        Integer numberOfDislike = memoryEntityRepository.countDislikeOutfitByStyleBodyShapeAndUserID(styleName, bodyShapeName, userID);

        Integer numberOfSuggest = memoryEntityRepository.countSuggestOutfitByStyleBodyShapeAndUserID(styleName, bodyShapeName, userID);

        Integer numberOfDisLikeAndSuggest = memoryEntityRepository.countDislikeAndSuggestOutfitByStyleBodyShapeAndUserID(styleName, bodyShapeName, userID);

        return  numberOfSuggest + numberOfDislike - numberOfDisLikeAndSuggest;
    }

    @Override
    public List<MemoryEntity> getAllMemoryEntityByStyleBodyShapeUserAcceptOldOutfit(String styleName, String bodyShapeName, String userID) throws CustomException {
        return memoryEntityRepository.getAllMemoryEntityByStyleBodyShapeUserAcceptOldOutfit(styleName, bodyShapeName, userID);
    }

    @Override
    public void updateAcceptOldOutfitsUntilNewOutfitArrive(String styleName, String bodyShapeName, String userID) throws CustomException {
        List<MemoryEntity> ListMemoryEntityByStyleBodyShapeAndUser = memoryEntityRepository.getAllMemoryEntityByStyleBodyShapeAndUser(styleName, bodyShapeName, userID);

        ListMemoryEntityByStyleBodyShapeAndUser.forEach(
                memoryEntity ->
                {
                    if(memoryEntity.getUserAcceptedOldOutfit() == null)
                    {
                        memoryEntityRepository.updateMemoryEntitiesWhenAcceptOldOutfit("," + userID, memoryEntity.getMemoryID());
                    }
                    else if(!memoryEntity.getUserAcceptedOldOutfit().contains("," + userID))
                    {
                        memoryEntityRepository.updateMemoryEntitiesWhenAcceptOldOutfit(memoryEntity.getUserAcceptedOldOutfit()  + userID, memoryEntity.getMemoryID());
                    }
                }
        );
    }
}
