package com.tttm.Whear.App.service.impl;

import com.tttm.Whear.App.constant.ConstantMessage;
import com.tttm.Whear.App.entity.BodyShape;
import com.tttm.Whear.App.entity.Style;
import com.tttm.Whear.App.enums.BodyShapeType;
import com.tttm.Whear.App.enums.StyleType;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.repository.BodyShapeRepository;
import com.tttm.Whear.App.service.BodyShapeService;
import com.tttm.Whear.App.utils.request.BodyShapeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BodyShapeServiceImpl implements BodyShapeService {
    private final BodyShapeRepository bodyShapeRepository;

    @Override
    public BodyShape getBodyShapeByBodyShapeName(String bodyShapeName) {
        return bodyShapeRepository.getBodyShapeByBodyShapeName(bodyShapeName.toUpperCase());
    }

    @Override
    public List<BodyShape> getAllBodyShape() {
        return bodyShapeRepository.findAll();
    }

    @Override
    public BodyShape getBodyShapeByID(Integer ID) throws CustomException {
        return bodyShapeRepository.findById(ID)
                .orElseThrow(() -> new CustomException(ConstantMessage.ID_IS_EMPTY_OR_NOT_EXIST.getMessage()));
    }

    @Override
    public void createBodyShape(BodyShapeRequest bodyShapeRequest) throws CustomException {
        if (bodyShapeRequest.getBodyShapeName() == null){
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }

        BodyShape bodyShape = BodyShape
                .builder()
                .bodyShapeName(BodyShapeType.valueOf(bodyShapeRequest.getBodyShapeName().toUpperCase()))
                .maxHeight(bodyShapeRequest.getMaxHeight())
                .minHeight(bodyShapeRequest.getMinHeight())
                .maxWeight(bodyShapeRequest.getMaxWeight())
                .minWeight(bodyShapeRequest.getMinWeight())
                .build();

        bodyShapeRepository.save(bodyShape);
    }
}
