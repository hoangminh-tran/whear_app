package com.tttm.Whear.App.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tttm.Whear.App.entity.Customer;
import com.tttm.Whear.App.entity.User;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.utils.request.UpdateBrandRequest;
import com.tttm.Whear.App.utils.response.UpdateBrandResponse;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface CustomerService {
    Customer createNewCustomers(User username);

    Customer getCustomerByID(String customerID);

    void updateNumberOfTimesChangeStyleByCustomerID(Integer numberOfUpdateStyle, String customerID);

    UpdateBrandResponse updateToBrand(UpdateBrandRequest updateBrandRequest) throws CustomException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException;
}
