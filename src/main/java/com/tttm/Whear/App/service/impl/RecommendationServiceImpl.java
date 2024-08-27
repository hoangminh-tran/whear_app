package com.tttm.Whear.App.service.impl;

import com.tttm.Whear.App.constant.ConstantMessage;
import com.tttm.Whear.App.dto.ClothesItemDto;
import com.tttm.Whear.App.dto.PairConsineSimilarity;
import com.tttm.Whear.App.dto.Pairs;
import com.tttm.Whear.App.dto.UserFollowDto;
import com.tttm.Whear.App.entity.User;
import com.tttm.Whear.App.enums.*;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.service.*;
import com.tttm.Whear.App.utils.request.FollowRequest;
import com.tttm.Whear.App.utils.response.ClothesResponse;
import com.tttm.Whear.App.utils.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {
    private static final double THRESHOLD_FOR_HISTORY_SEARCH = 0.055555555555555555555555;
    private static final double THRESHOLD_FOR_FOLLOW_FEATURE = 0.33333333333333333333333333;

    private final UserService userService;

    private final HistoryService historyService;

    private final ClothesService clothesService;

    private final FollowService followService;
    private final UserStyleService userStyleService;
    private final ReactService reactService;
    private final ClothesDataService clothesDataService;

    @Override
    public List<ClothesResponse> getListRecommendationByUserHistoryItems(String userID) throws CustomException {
        Optional.of(userID)
                .filter(id -> !id.isEmpty() && !id.isBlank())
                .orElseThrow(() -> new CustomException(ConstantMessage.USERID_IS_EMPTY_OR_NOT_EXIST.getMessage()));

        User user = Optional.ofNullable(userService.getUserEntityByUserID(userID))
                .orElseThrow(() -> new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage()));

        String userSearchText = convertListHistoryItemsToText(userID);
        List<Pairs> clothesItemList = clothesDataService.getClothesItemList();
        List<PairConsineSimilarity> listConsineSimilarity = calculateConsineSimilarities(userSearchText, clothesItemList);
        Collections.sort(listConsineSimilarity, Comparator.comparingDouble(PairConsineSimilarity::getConsineSimilarity).reversed());
        return listConsineSimilarity.stream()
                .filter(similarityPoint -> similarityPoint.getConsineSimilarity() >= THRESHOLD_FOR_HISTORY_SEARCH)
                .map(similarityPoint -> {
                    try {
                        ClothesResponse response = clothesDataService.getClothesResponseList().get(similarityPoint.getClothesID());
                        response.setReacted(
                                reactService.checkContain(similarityPoint.getClothesID(), userID) != null
                        );
                        return response;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ClothesResponse> getListRecommendationByKeyword(String userID, String keyword) throws CustomException {
        Optional.of(userID)
                .filter(id -> !id.isEmpty() && !id.isBlank())
                .orElseThrow(() -> new CustomException(ConstantMessage.USERID_IS_EMPTY_OR_NOT_EXIST.getMessage()));

        User user = Optional.ofNullable(userService.getUserEntityByUserID(userID))
                .orElseThrow(() -> new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage()));

        historyService.createHistoryItemByDefaultStyleOrKeyword(userID, keyword, "2");
        List<Pairs> clothesItemList = clothesDataService.getClothesItemList();
        List<PairConsineSimilarity> listConsineSimilarity = calculateConsineSimilarities(keyword, clothesItemList);
        Collections.sort(listConsineSimilarity, Comparator.comparingDouble(PairConsineSimilarity::getConsineSimilarity).reversed());

//        for (PairConsineSimilarity pairs : listConsineSimilarity) {
//            System.out.println(clothesService.getClothesByID(pairs.getClothesID()) + " " + pairs.getConsineSimilarity());
//        }

        return listConsineSimilarity.stream()
                .filter(similarityPoint -> similarityPoint.getConsineSimilarity() >= THRESHOLD_FOR_HISTORY_SEARCH)
                .map(similarityPoint -> {
                    try {
                        ClothesResponse response = clothesDataService.getClothesResponseList().get(similarityPoint.getClothesID());
                        response.setReacted(
                                reactService.checkContain(similarityPoint.getClothesID(), userID) != null
                        );
                        return response;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

    }

    @Override
    public List<UserResponse> getListRecommendationUserWhenUserFollowAnotherUser(FollowRequest followRequest) throws CustomException {
        // Get List of Follower of the Target User when Base User follow Target User
        if (followRequest.getBaseUserID() == null
                || followRequest.getBaseUserID().isBlank()
                || followRequest.getBaseUserID().isEmpty()

                || followRequest.getTargetUserID() == null
                || followRequest.getTargetUserID().isBlank()
                || followRequest.getTargetUserID().isEmpty()
        ) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }

        User baseUserEntity = Optional.ofNullable(userService.getUserEntityByUserID(followRequest.getBaseUserID()))
                .orElseThrow(() -> new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage() + ": "
                        + followRequest.getBaseUserID()));

        User targetUserEntity = Optional.ofNullable(userService.getUserEntityByUserID(followRequest.getTargetUserID()))
                .orElseThrow(() -> new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage() + ": "
                        + followRequest.getTargetUserID()));

        String baseUserHistoryText = convertListHistoryItemsToText(followRequest.getBaseUserID());

        List<UserResponse> listFollowingUser = followService.getAllFollowingUserExceptCurrentUser(followRequest.getTargetUserID(), followRequest.getBaseUserID());

        List<String> listBaseHistoryStyle = historyService.getAllHistoryItemByUserIDAnIndex(followRequest.getBaseUserID(), "1");

        List<UserFollowDto> userFollowDtoList = listFollowingUser.stream()
                .map(user -> {
                    try {
                        List<String> listTargetHistoryStyle = historyService.getAllHistoryItemByUserIDAnIndex(user.getUserID(), "1");
                        double consineSimilarityForStyle = calculateConsineSimilarityByTwoStyleFromTwoUser(listBaseHistoryStyle, listTargetHistoryStyle);
                        System.out.println(consineSimilarityForStyle);
                        if (consineSimilarityForStyle <= 0.0) return null;

                        String followingUserHistoryText = convertListHistoryItemsToText(user.getUserID());
                        double consineSimilarity = calculateConsineSimilarityByTwoUserHistorySearch(baseUserHistoryText, followingUserHistoryText);
                        Long totalOfFollowerOfTargetUserID = followService.calculateNumberOfFollowerByUserID(user.getUserID());
                        Long totalOfFollowingOfTargetUserID = followService.calculateNumberOfFollowingByUserID(user.getUserID());
                        return UserFollowDto
                                .builder()
                                .followingUserID(user.getUserID())
                                .consineSimilarity(consineSimilarity)
                                .totalOfFollowerOfTargetUserID(totalOfFollowerOfTargetUserID)
                                .totalOfFollowingOfTargetUserID(totalOfFollowingOfTargetUserID)
                                .build();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Collections.sort(userFollowDtoList, (u1, u2) -> {
            if (u1.getConsineSimilarity() < THRESHOLD_FOR_FOLLOW_FEATURE) {
                return Long.compare(u2.getTotalOfFollowerOfTargetUserID(), u1.getTotalOfFollowerOfTargetUserID());
            }
            return Double.compare(u2.getConsineSimilarity(), u1.getConsineSimilarity());
        });

        userFollowDtoList.forEach(userFollowDto -> System.out.println(userFollowDto));

        return userFollowDtoList
                .stream()
                .map(user -> {
                    try {
                        return userService.convertToUserResponse(userService.getUserEntityByUserID(user.getFollowingUserID()));
                    } catch (CustomException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    private double calculateConsineSimilarityByTwoStyleFromTwoUser(List<String> baseUserStyle, List<String> targetUserStyle) {
        double dotStyle = 0.0, magnitudeStyleBaseUser = 0.0, magnitudeStyleTargetUser = 0.0;
        for (String userStyle : baseUserStyle) {
            if (targetUserStyle.contains(userStyle)) {
                dotStyle += 1;
            }
            magnitudeStyleBaseUser += Math.pow(1, 2);
        }
        for (String userStyle : targetUserStyle) {
            magnitudeStyleTargetUser += Math.pow(1, 2);
        }
        return dotStyle / (Math.sqrt(magnitudeStyleBaseUser) * Math.sqrt(magnitudeStyleTargetUser));
    }

    public List<PairConsineSimilarity> calculateConsineSimilarities(String userSearchText, List<Pairs> listClothesText) {
        List<PairConsineSimilarity> listConsineSimilarity = new ArrayList<>();
        Map<String, Integer> userHistorySearchVector = createFequencyAppearanceVector(userSearchText);
        for (Pairs Items : listClothesText) {
            Map<String, Integer> clothesItemVector = createFequencyAppearanceVector(Items.getClothesItem());
            double consineSimilarity = calculateCosineSimilarity(userHistorySearchVector, clothesItemVector);
            listConsineSimilarity.add(new PairConsineSimilarity(Items.getClothesID(), consineSimilarity));
        }
        return listConsineSimilarity;
    }

    private double calculateConsineSimilarityByTwoUserHistorySearch(String baseUserHistoryText, String targetUserHistoryText) {
        Map<String, Integer> baseUserHistoryVector = createFequencyAppearanceVector(baseUserHistoryText);
        Map<String, Integer> targetUserHistoryVector = createFequencyAppearanceVector(targetUserHistoryText);
        return calculateCosineSimilarity(baseUserHistoryVector, targetUserHistoryVector);
    }

    private Map<String, Integer> createFequencyAppearanceVector(String text) {
        Map<String, Integer> map = new HashMap<>();
        String[] listKeyWords = text.toUpperCase().split("\\s+");
        for (String keywords : listKeyWords) {
            map.put(keywords, map.getOrDefault(keywords, 0) + 1);
        }
        return map;
    }

    private Double calculateCosineSimilarity(Map<String, Integer> userVector, Map<String, Integer> clothesVector) {
        double dotClothes = 0.0, magnitudeOfUser = 0.0, magnitudeOfClothes = 0.0;
        for (String keyWords : userVector.keySet()) {
            if (clothesVector.containsKey(keyWords)) // Check whether the keywords is appear between UserHistorySearch and ProductList
            {
                dotClothes += userVector.get(keyWords) * clothesVector.get(keyWords);
            }
            magnitudeOfUser += Math.pow(userVector.get(keyWords), 2);
        }

        for (int value : clothesVector.values()) {
            magnitudeOfClothes += Math.pow(value, 2);
        }

        return dotClothes / (Math.sqrt(magnitudeOfUser) * Math.sqrt(magnitudeOfClothes));
    }

    private String convertListHistoryItemsToText(String userID) throws CustomException {
        List<String> historyItems = historyService.getAllHistoryItemsByCustomerID(userID).getHistoryItems()
                .stream()
                .map(items -> items.toUpperCase())
                .collect(Collectors.toList());
        return String.join(" ", historyItems);
    }

    public List<Pairs> convertListClothesToListClothesPairs() throws CustomException {
        List<ClothesResponse> responses = clothesDataService.getClothesResponseList();

        List<ClothesResponse> filterClothesResponse = responses
                .stream()
                .skip(67)
                .collect(Collectors.toList());

        List<ClothesItemDto> ClothesItemDtoList = filterClothesResponse
                .stream()
                .map(this::convertToClothesItemDto)
                .collect(Collectors.toList());

        return ClothesItemDtoList.stream()
                .map(clothes -> {
                    String ClotheItems = clothes.getNameOfProduct().toUpperCase() + " " + clothes.getTypeOfClothes() + " " + clothes.getShape() + " " +
                            clothes.getMaterials() + " " + clothes.seasonToString() + " " + clothes.sizeToString() + " " + clothes.colorToString() + " " +
                            clothes.styleToString();
                    return new Pairs(clothes.getClothesID(), ClotheItems);
                })
                .collect(Collectors.toList());
    }

    public ClothesItemDto convertToClothesItemDto(ClothesResponse clothesResponse) {
        return ClothesItemDto
                .builder()
                .clothesID(clothesResponse.getClothesID())
                .nameOfProduct(clothesResponse.getNameOfProduct())
                .typeOfClothes(ClothesType.valueOf(clothesResponse.getTypeOfClothes()))
                .shape(ShapeType.valueOf(clothesResponse.getShape()))
                .materials(MaterialType.valueOf(clothesResponse.getMaterials()))
                .seasons(
                        clothesResponse
                                .getClothesSeasons()
                                .stream()
                                .map(Season -> SeasonType.valueOf(Season))
                                .toList()
                )
                .sizes(
                        clothesResponse
                                .getClothesSizes()
                                .stream()
                                .map(Size -> SizeType.valueOf(Size))
                                .toList()
                )
                .colors(
                        clothesResponse
                                .getClothesColors()
                                .stream()
                                .map(Colors -> ColorType.valueOf(Colors))
                                .toList()
                )
                .styles(
                        clothesResponse
                                .getClothesStyles()
                                .stream()
                                .map(Styles -> StyleType.valueOf(Styles))
                                .toList()
                )
                .build();
    }

}
