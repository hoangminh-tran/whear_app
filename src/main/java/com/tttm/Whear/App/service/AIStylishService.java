package com.tttm.Whear.App.service;

import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.utils.request.CalculateOutfitsRequest;
import com.tttm.Whear.App.utils.request.RejectClothesRequest;
import com.tttm.Whear.App.utils.request.SuggestChoiceForPremiumUser;
import com.tttm.Whear.App.utils.response.AIStylishResponse;
import com.tttm.Whear.App.utils.response.CalculateOutfitsResponse;

import java.util.List;

public interface AIStylishService {
    List<AIStylishResponse> getSuggestClothesForUser(String userID) throws CustomException;
    AIStylishResponse createNewClothesAfterRejectClothesForPremiumUser(RejectClothesRequest rejectClothesRequest) throws CustomException;
    List<AIStylishResponse> selectChoiceWhenRunOutOfOutfitsForPremium(SuggestChoiceForPremiumUser suggestChoiceForPremiumUser) throws CustomException;

    CalculateOutfitsResponse calculateMaximumOutfitsCanGenerate(CalculateOutfitsRequest calculateOutfitsRequest) throws CustomException;
}
