package com.tttm.Whear.App.service;

import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.utils.request.HistoryRequest;
import com.tttm.Whear.App.utils.response.HistoryResponse;

import java.util.List;

public interface HistoryService {
    void createHistoryItemBySearching(HistoryRequest historyRequest) throws CustomException;

    HistoryResponse getAllHistoryItemsByCustomerID(String customerID) throws CustomException;

    List<String> createHistoryItemBasedOnReactFeature(String userID, Integer clothesID) throws CustomException;

    void deleteHistoryItemBasedOnReactFeature(String userID, Integer clothesID) throws CustomException;

    void createHistoryItemByDefaultStyleOrKeyword(String userID, String styleName, String typeOfIndex);

    void updateHistoryByNewStyle(String newStyleName, String customerID, String oldStyleName, String index);

    List<String> getAllHistoryItemByUserIDAnIndex(String userID, String index);

}
