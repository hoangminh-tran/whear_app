package com.tttm.Whear.App.service.impl;

import com.tttm.Whear.App.constant.ConstantMessage;
import com.tttm.Whear.App.constant.ConstantString;
import com.tttm.Whear.App.entity.Customer;
import com.tttm.Whear.App.entity.Style;
import com.tttm.Whear.App.entity.User;
import com.tttm.Whear.App.entity.UserStyle;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.repository.UserStyleRepository;
import com.tttm.Whear.App.service.*;
import com.tttm.Whear.App.utils.request.StyleAndBodyShapeRequest;
import com.tttm.Whear.App.utils.request.UpdateStyleRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserStyleServiceImpl implements UserStyleService {
    private final UserStyleRepository userStyleRepository;
    private final UserService userService;
    private final HistoryService historyService;
    private final StyleService styleService;
    private final CustomerService customerService;
    private final AIStylishService aiStylishService;
    @Override
    @Transactional
    public void createStyleAndBodyShape(StyleAndBodyShapeRequest request) throws CustomException {
        Optional.of(request.getUserID())
                .filter(id -> !id.isEmpty() && !id.isBlank())
                .orElseThrow(() -> new CustomException(ConstantMessage.USERID_IS_EMPTY_OR_NOT_EXIST.getMessage()));

        User user = Optional.ofNullable(userService.getUserEntityByUserID(request.getUserID()))
                .orElseThrow(() -> new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage() + " : " + request.getUserID()));

        if(request.getBodyShapeName().isBlank() || request.getBodyShapeName().isEmpty())
        {
            throw new CustomException("Body Shape can not be null");
        }

        if(request.getListStyles().size() == 0)
        {
            throw new CustomException("Style can not be null");
        }

        userService.updateBodyShapeToUser(user, request.getBodyShapeName());

        request.getListStyles().forEach(styleName -> {
            Style style = styleService.getStyleByStyleName(styleName);
            if (style == null) {
                throw new RuntimeException(new CustomException(ConstantMessage.CAN_NOT_FIND_STYLE_NAME.getMessage() + " : " + styleName));
            }

            if (userStyleRepository.findUserStyleByStyleIDAndUserID(style.getStyleID(), request.getUserID()) == null) {
                historyService.createHistoryItemByDefaultStyleOrKeyword(request.getUserID(), styleName, "1");
                userStyleRepository.createUserStyle(request.getUserID(), style.getStyleID(), userStyleRepository.findAll().size() + 1);
            } else {
                throw new RuntimeException(new CustomException(ConstantMessage.STYLE_ID_AND_USER_ID_IS_EXIST.getMessage()));
            }
        });
    }


    @Override
    @Transactional
    public void updateStyleForCustomer(UpdateStyleRequest request) throws CustomException {
        Optional.of(request.getUserID())
                .filter(id -> !id.isEmpty() && !id.isBlank())
                .orElseThrow(() -> new CustomException(ConstantMessage.USERID_IS_EMPTY_OR_NOT_EXIST.getMessage()));

        Optional.of(request.getOldStyleName())
                .filter(id -> !id.isEmpty() && !id.isBlank())
                .orElseThrow(() -> new CustomException("Old Style Name can not be empty or blank"));

        Optional.of(request.getNewStyleName())
                .filter(id -> !id.isEmpty() && !id.isBlank())
                .orElseThrow(() -> new CustomException("New Style Name can not be empty or blank"));

        User user = Optional.ofNullable(userService.getUserEntityByUserID(request.getUserID()))
                .orElseThrow(() -> new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERID + " : " + request.getUserID()));

        Customer customer = Optional.ofNullable(customerService.getCustomerByID(user.getUserID()))
                .orElseThrow(() -> new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERID + " : " + request.getUserID()));

        if(customer.getSubRoleID() == 1 && customer.getNumberOfUpdateStyle() == ConstantString.MAXIMUM_NUMBER_TIMES_CAN_UPDATE_STYLE_FOR_FREE_USER)
        {
            throw new CustomException(ConstantMessage.ACCOUNT_HAVE_CHANGE_STYLE_MAXIMUM_NUMBER_OF_TIMES.getMessage());
        }

        if(request.getOldStyleName().trim().toUpperCase().equals(request.getNewStyleName().trim().toUpperCase()))
        {
            throw new CustomException(ConstantMessage.OLD_STYLE_AND_NEW_STYLE_IS_THE_SAME.getMessage());
        }

        // Find Old Style
        Style oldStyle = Optional.ofNullable(styleService.getStyleByStyleName(request.getOldStyleName()))
                .orElseThrow(() -> new CustomException(ConstantMessage.CAN_NOT_FIND_STYLE_NAME.getMessage() + " : " + request.getOldStyleName()));

        // Find New Style
        Style newStyle = Optional.ofNullable(styleService.getStyleByStyleName(request.getNewStyleName()))
                .orElseThrow(() -> new CustomException(ConstantMessage.CAN_NOT_FIND_STYLE_NAME.getMessage() + " : " + request.getNewStyleName()));

        var userStyle = getListUserStyleByUserID(request.getUserID());

        if(!userStyle.stream().anyMatch(style -> style.getUserStyleKey().getStyleID().equals(oldStyle.getStyleID())))
        {
            throw new CustomException(ConstantMessage.STYLE_NAME_IS_NOT_EXIST_IN_USER_STYLE.getMessage());
        }

        if (userStyle.stream().anyMatch(style -> style.getUserStyleKey().getStyleID().equals(newStyle.getStyleID())))
        {
            throw new CustomException(ConstantMessage.STYLE_NAME_IS_EXIST_IN_USER_STYLE.getMessage());
        }

        // Update new Style to UserStyle and History
        userStyleRepository.updateUserStyle(newStyle.getStyleID(), request.getUserID(), oldStyle.getStyleID());
        historyService.updateHistoryByNewStyle(request.getNewStyleName(), request.getUserID(), request.getOldStyleName(), "1");
        customerService.updateNumberOfTimesChangeStyleByCustomerID(customer.getNumberOfUpdateStyle() + 1, customer.getCustomerID());
    }

    @Override
    public List<UserStyle> getListUserStyleByUserID(String userID) throws CustomException {
        return userStyleRepository.getListUserStyleByUserID(userID);
    }
}
