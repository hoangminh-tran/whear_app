package com.tttm.Whear.App.service;


import com.tttm.Whear.App.entity.BodyShape;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.utils.request.BodyShapeRequest;

import java.util.List;

public interface BodyShapeService {
    BodyShape getBodyShapeByBodyShapeName(String bodyShapeName);

    List<BodyShape> getAllBodyShape();

    BodyShape getBodyShapeByID(Integer ID) throws CustomException;

    void createBodyShape(BodyShapeRequest bodyShapeRequest) throws CustomException;
}
