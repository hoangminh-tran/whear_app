package com.tttm.Whear.App.service;

import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.utils.request.ClothesRequest;
import com.tttm.Whear.App.utils.request.UserRequest;
import com.tttm.Whear.App.utils.response.ClothesResponse;

import java.util.List;

public interface GenerateDataService {
    List<ClothesRequest> generateRandomListClothes(String userID, int size) throws CustomException;
    List<String> generateRandomHistoryUserSearch(String userID, int size) throws CustomException;

    void generateRandomCustomer() throws CustomException;
}
