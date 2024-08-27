package com.tttm.Whear.App.service.impl;

import com.tttm.Whear.App.constant.ConstantMessage;
import com.tttm.Whear.App.entity.Style;
import com.tttm.Whear.App.enums.StyleType;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.repository.StyleRepository;
import com.tttm.Whear.App.service.StyleService;
import com.tttm.Whear.App.utils.request.StyleRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StyleServiceImpl implements StyleService {
    private final StyleRepository styleRepository;

    @Override
    public Style getStyleByStyleName(String styleName) {
        return styleRepository.getStyleByStyleName(styleName);
    }

    @Override
    public List<Style> getAllStyle() {
        return styleRepository.findAll();
    }

    @Override
    public Style getStyleByID(Integer ID) throws CustomException {
        return styleRepository.findById(ID)
                .orElseThrow(() -> new CustomException(ConstantMessage.ID_IS_EMPTY_OR_NOT_EXIST.getMessage()));
    }

    @Override
    public List<Style> getListStyleByUserID(String userID) throws CustomException {
        return styleRepository.getListStyleNameByUserID(userID);
    }

    @Override
    public void createStyle(StyleRequest styleRequest) throws CustomException {
        if (styleRequest.getStyleName() == null){
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }

        Style style = Style
                .builder()
                .styleName(StyleType.valueOf(styleRequest.getStyleName().toUpperCase()))
                .build();

        styleRepository.save(style);
    }
}
