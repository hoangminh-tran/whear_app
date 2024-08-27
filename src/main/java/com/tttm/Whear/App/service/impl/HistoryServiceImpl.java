package com.tttm.Whear.App.service.impl;

import com.tttm.Whear.App.constant.ConstantMessage;
import com.tttm.Whear.App.entity.History;
import com.tttm.Whear.App.entity.User;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.repository.HistoryRepository;
import com.tttm.Whear.App.service.ClothesService;
import com.tttm.Whear.App.service.HistoryService;
import com.tttm.Whear.App.service.UserService;
import com.tttm.Whear.App.utils.request.HistoryRequest;
import com.tttm.Whear.App.utils.response.ClothesResponse;
import com.tttm.Whear.App.utils.response.HistoryResponse;
import com.tttm.Whear.App.utils.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {
    private final HistoryRepository historyRepository;

    private final UserService userService;

    private final ClothesService clothesService;
    private final Logger logger = LoggerFactory.getLogger(HistoryServiceImpl.class);
    @Override
    public void createHistoryItemBySearching(HistoryRequest historyRequest) throws CustomException {
        Optional.of(historyRequest.getCustomerID())
                .filter(id -> !id.isEmpty() && !id.isBlank())
                .orElseThrow(() -> new CustomException(ConstantMessage.USERID_IS_EMPTY_OR_NOT_EXIST.getMessage()));

        User user = userService.getUserEntityByUserID(historyRequest.getCustomerID());
        if (user == null) {
            throw new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage());
        }

        Set<String> historiesSet = historyRepository.getAllHistoryItemsByCustomerID(historyRequest.getCustomerID())
                .stream()
                .map(history -> history.getHistoryItem().toString().toUpperCase())
                .collect(Collectors.toSet());
        
        historyRequest
                .getHistoryItems()
                .forEach(historyItem -> {
                        if (!historiesSet.contains(historyItem.toUpperCase())) {
                            historiesSet.add(historyItem.toUpperCase());
                            historyRepository.createHistoryItem(
                                    historyRequest.getCustomerID(),
                                    historyItem.toUpperCase(),
                                    "2"
                            );
                        }
                    }
                );
    }

    @Override
    public void createHistoryItemByDefaultStyleOrKeyword(String userID, String styleName, String typeOfIndex) {
        historyRepository.createHistoryItem(userID, styleName, typeOfIndex);
    }

    @Override
    public void updateHistoryByNewStyle(String newStyleName, String customerID, String oldStyleName, String index) {
        historyRepository.updateHistoryByNewStyle(newStyleName, customerID, oldStyleName, index);
    }

    @Override
    public List<String> getAllHistoryItemByUserIDAnIndex(String userID, String index) {
        return historyRepository.getAllHistoryItemByUserIDAnIndex(userID, index)
                .stream()
                .map(history -> history.getHistoryItem())
                .collect(Collectors.toList());
    }

    @Override
    public List<String> createHistoryItemBasedOnReactFeature(String userID, Integer clothesID) throws CustomException {
        ClothesResponse clothesResponse = clothesService.getClothesByID(clothesID);

        List<String> historyReact = Stream.of(
                        clothesResponse.getNameOfProduct(),
                        clothesResponse.getTypeOfClothes(),
                        clothesResponse.getShape(),
                        clothesResponse.getMaterials())
                .collect(Collectors.toList());

        clothesResponse.getClothesSeasons()
                .forEach(
                        season -> historyReact.add(season)
                );

        clothesResponse.getClothesColors()
                .forEach(
                        color -> historyReact.add(color)
                );

        clothesResponse.getClothesStyles()
                .forEach(
                        style -> historyReact.add(style)
                );

        historyReact.forEach(history -> {
            if(!history.equals("X")){
                historyRepository.createHistoryItem(userID, history, "3_" + clothesResponse.getClothesID());
            }
        });

        return historyReact;
    }

    @Override
    public void deleteHistoryItemBasedOnReactFeature(String userID, Integer clothesID) throws CustomException {
        historyRepository.deleteAllHistoryItems(userID, "3_" + clothesID);
    }

    @Override
    public HistoryResponse getAllHistoryItemsByCustomerID(String customerID) throws CustomException {
        List<String> historyItems = historyRepository
                .getAllHistoryItemsByCustomerID(customerID)
                .stream()
                .map(history -> history.getHistoryItem())
                .toList();

        return convertToHistoryResponse(customerID, historyItems);
    }

    public HistoryResponse convertToHistoryResponse(String customerID, List<String> historyItem) {
        return HistoryResponse
                .builder()
                .customerID(customerID)
                .historyItems(historyItem)
                .build();

    }
}
