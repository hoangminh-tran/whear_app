package com.tttm.Whear.App.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.utils.request.BrandRequestDto;
import com.tttm.Whear.App.utils.response.BrandResponse;
import com.tttm.Whear.App.utils.response.HotBrandResponse;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface BrandService {
    BrandResponse createNewBrand(BrandRequestDto brandRequestDto) throws CustomException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException;

    BrandResponse getBrandByID(Integer brandID) throws CustomException;

    List<HotBrandResponse> getListHotBrand() throws CustomException;
}
