package com.tttm.Whear.App.service;

import com.tttm.Whear.App.entity.UserStyle;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.utils.request.StyleAndBodyShapeRequest;
import com.tttm.Whear.App.utils.request.UpdateStyleRequest;
import com.tttm.Whear.App.utils.request.UserRequest;

import java.util.List;

public interface UserStyleService {
    void createStyleAndBodyShape(StyleAndBodyShapeRequest request) throws CustomException;

    void updateStyleForCustomer(UpdateStyleRequest updateStyleRequest) throws CustomException;

    List<UserStyle> getListUserStyleByUserID(String userID) throws CustomException;
}
