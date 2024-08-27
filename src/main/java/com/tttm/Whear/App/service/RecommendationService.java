package com.tttm.Whear.App.service;

import com.tttm.Whear.App.dto.ClothesItemDto;
import com.tttm.Whear.App.dto.PairConsineSimilarity;
import com.tttm.Whear.App.dto.Pairs;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.utils.request.ClothesRequest;
import com.tttm.Whear.App.utils.request.FollowRequest;
import com.tttm.Whear.App.utils.request.HistorySearchRequest;
import com.tttm.Whear.App.utils.response.ClothesResponse;
import com.tttm.Whear.App.utils.response.UserResponse;

import java.util.List;

public interface RecommendationService {
      List<ClothesResponse> getListRecommendationByUserHistoryItems(String userID) throws CustomException;

      List<ClothesResponse> getListRecommendationByKeyword(String userID, String keyword) throws CustomException;
      List<UserResponse> getListRecommendationUserWhenUserFollowAnotherUser(FollowRequest followRequest) throws CustomException;
      public List<Pairs> convertListClothesToListClothesPairs() throws CustomException;
      public ClothesItemDto convertToClothesItemDto(ClothesResponse clothesResponse);
}
