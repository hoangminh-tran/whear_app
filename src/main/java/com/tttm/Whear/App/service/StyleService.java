package com.tttm.Whear.App.service;

import com.tttm.Whear.App.entity.Style;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.utils.request.StyleRequest;

import java.util.List;

public interface StyleService {
    Style getStyleByStyleName(String styleName);

    List<Style> getAllStyle();

    Style getStyleByID(Integer ID) throws CustomException;
    List<Style> getListStyleByUserID(String UserID) throws CustomException;

    void createStyle(StyleRequest styleRequest) throws CustomException;
}
