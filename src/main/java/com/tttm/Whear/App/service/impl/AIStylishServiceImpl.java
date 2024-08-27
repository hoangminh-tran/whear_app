package com.tttm.Whear.App.service.impl;

import com.tttm.Whear.App.constant.ConstantMessage;
import com.tttm.Whear.App.constant.ConstantString;
import com.tttm.Whear.App.entity.*;
import com.tttm.Whear.App.enums.ESubRole;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.service.*;
import com.tttm.Whear.App.utils.request.CalculateOutfitsRequest;
import com.tttm.Whear.App.utils.request.MemoryRequest;
import com.tttm.Whear.App.utils.request.RejectClothesRequest;
import com.tttm.Whear.App.utils.request.SuggestChoiceForPremiumUser;
import com.tttm.Whear.App.utils.response.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AIStylishServiceImpl implements AIStylishService {
    private final UserService userService;
    private final ClothesService clothesService;
    private final CustomerService customerService;
    private final RuleMatchingClothesService ruleMatchingClothesService;
    private final SubroleService subroleService;
    private final BodyShapeService bodyShapeService;
    private final StyleService styleService;
    private final MemoryEntityService memoryEntityService;
    private final Random random = new Random();
    private final ClothesDataService clothesDataService;
    private final Logger logger = LoggerFactory.getLogger(AIStylishServiceImpl.class);


    @Override
    public List<AIStylishResponse> getSuggestClothesForUser(String userID) throws CustomException {
        // Check ID User or User Entity is exist or not
        Optional.of(userID)
                .filter(id -> !id.isEmpty() && !id.isBlank())
                .orElseThrow(() -> new CustomException(ConstantMessage.USERID_IS_EMPTY_OR_NOT_EXIST.getMessage()));

        User user = Optional.ofNullable(userService.getUserEntityByUserID(userID))
                .orElseThrow(() -> new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage()));

        // Get BodyShape by Specific User
        BodyShape bodyShape = user.getBodyShape();

        // Get List Style By Specific User
        List<Style> styleList = styleService.getListStyleByUserID(userID);

        // Check SubRole is Free or Premium
        Customer customer = customerService.getCustomerByID(userID);
        List<AIStylishResponse> outfitsForWeek = new ArrayList<>();

        UserResponseStylish userResponseStylish = UserResponseStylish
                .builder()
                .userID(user.getUserID())
                .username(user.getUsername())
                .imgUrl(user.getImgUrl())
                .build();

        switch (subroleService.getSubroleByID(customer.getSubRoleID()).getSubRoleName()) {
            case LV1:
                outfitsForWeek = suggestClothesForFreeUser(user, ESubRole.LV1.toString(), bodyShape, styleList, userResponseStylish);
                break;
            case LV2:
                outfitsForWeek = suggestClothesForPremiumUser(user, ESubRole.LV2.toString(), bodyShape, styleList, userResponseStylish);
                break;
        }
        return outfitsForWeek;
    }

    private List<AIStylishResponse> suggestClothesForFreeUser(User user,
                                                              String subRole,
                                                              BodyShape bodyShape,
                                                              List<Style> styleList,
                                                              UserResponseStylish userResponseStylish) throws CustomException {
        List<RuleMatchingClothesResponse> ruleMatchingClothesResponses = styleList
                .stream()
                .map(style -> ruleMatchingClothesService.getRuleMatchingClothesByStyleAndBodyShape(style.getStyleID(), bodyShape.getBodyShapeID()))
                .collect(Collectors.toList());

        String userGender = (user.getGender() == true) ? "MALE" : "FEMALE";

        List<AIStylishResponse> aiStylishResponses = new ArrayList<>();
        int index = 0;

        for (RuleMatchingClothesResponse rule : ruleMatchingClothesResponses) {
            List<List<ClothesResponse>> outfitList = new ArrayList<>();

            List<ClothesResponse> topInsideClothes = clothesDataService.getClothesBaseOnTypeOfClothesAndColorOrMaterials(
                    rule.getTopInside(), rule.getTopInsideColor(), rule.getTopMaterial(), userGender);

//            List<ClothesResponse> topInsideClothes = clothesService.getClothesBaseOnTypeOfClothesAndColorOrMaterials(
//                    rule.getTopInside(), rule.getTopInsideColor(), rule.getTopMaterial());

            for (ClothesResponse clothesResponse : topInsideClothes) {
                logger.warn("This is Top Inside Clothes {}", clothesResponse);
            }

            List<ClothesResponse> topOutsideClothes = clothesDataService.getClothesBaseOnTypeOfClothesAndColorOrMaterials(
                    rule.getTopOutside(), rule.getTopOutsideColor(), rule.getTopMaterial(), userGender);
//            List<ClothesResponse> topOutsideClothes = clothesService.getClothesBaseOnTypeOfClothesAndColorOrMaterials(
//                    rule.getTopOutside(), rule.getTopOutsideColor(), rule.getTopMaterial());

            for (ClothesResponse clothesResponse : topOutsideClothes) {
                logger.warn("This is Top Outside Clothes {}", clothesResponse);
            }

            List<ClothesResponse> bottomClothes = clothesDataService.getClothesBaseOnTypeOfClothesAndColorOrMaterials(
                    rule.getBottomKind(), rule.getBottomColor(), rule.getBottomMaterial(), userGender);

//            List<ClothesResponse> bottomClothes = clothesService.getClothesBaseOnTypeOfClothesAndColorOrMaterials(
//                    rule.getBottomKind(), rule.getBottomColor(), rule.getBottomMaterial());

            for (ClothesResponse clothesResponse : bottomClothes) {
                logger.warn("This is Bottom Clothes {}", clothesResponse);
            }

            List<ClothesResponse> shoesClothes = clothesDataService.getClothesBaseOnTypeOfClothesAndColorOrMaterials(
                    rule.getShoesType(), rule.getShoesTypeColor(), rule.getBottomMaterial(), userGender);

//            List<ClothesResponse> shoesClothes = clothesService.getClothesBaseOnTypeOfClothesAndColorOrMaterials(
//                    rule.getShoesType(), rule.getShoesTypeColor(), rule.getBottomMaterial());

            for (ClothesResponse clothesResponse : shoesClothes) {
                logger.warn("This is Shoes Clothes {}", clothesResponse);
            }

            List<ClothesResponse> accessoriesClothes = clothesDataService.getClothesBaseOnTypeOfClothesAndMaterial(
                    rule.getAccessoryKind(), rule.getAccessoryMaterial(), userGender);
//            List<ClothesResponse> accessoriesClothes = clothesService.getClothesBaseOnTypeOfClothesAndMaterial(
//                    rule.getAccessoryKind(), rule.getAccessoryMaterial());

            for (ClothesResponse clothesResponse : accessoriesClothes) {
                logger.warn("This is Accessories Clothes {}", clothesResponse);
            }

            if (styleList.size() >= 2) index++;
            String message = "";

            List<List<ClothesResponse>> outfits = selectUniqueOutfits(
                    topInsideClothes,
                    topOutsideClothes,
                    bottomClothes,
                    shoesClothes,
                    accessoriesClothes,
                    rule.getTopInsideColor(),
                    rule.getTopOutsideColor(),
                    rule.getBottomColor(),
                    rule.getShoesTypeColor(),
                    rule.getStyleName(),
                    rule.getBodyShapeName(),
                    user.getUserID(),
                    subRole,
                    ConstantString.SUGGEST_OUTFITS_FOR_USER,
                    styleList.size() < 2 ? ConstantString.SUGGEST_CLOTHES_FOR_FREE_USER_A_WEEK : ConstantString.SUGGEST_CLOTHES_FOR_FREE_USER_A_DAY,
                    userResponseStylish);

            if (outfits.size() == (styleList.size() < 2 ? ConstantString.SUGGEST_CLOTHES_FOR_FREE_USER_A_WEEK : ConstantString.SUGGEST_CLOTHES_FOR_FREE_USER_A_DAY)) {
                message = ConstantMessage.SUGGEST_FULL_CLOTHES_FOR_FREE_USER.getMessage();
            } else {
                message = ConstantMessage.SUGGEST_MISSING_OR_RUN_OUT_OF_CLOTHES_FOR_FREE_USER.getMessage();
            }

            for (List<ClothesResponse> out : outfits) {
                if (!containsOutfit(outfitList, out)) {
                    outfitList.add(out);
                }
            }

            for (List<ClothesResponse> outfit : outfitList) {
                logger.error("This is Outfit: ");
                for (ClothesResponse clothes : outfit) {
                    logger.warn("This is clothes from Outfit {}", clothes);
                }
            }

            aiStylishResponses.add(
                    AIStylishResponse
                            .builder()
                            .styleName(rule.getStyleName())
                            .bodyShapeName(rule.getBodyShapeName())
                            .outfits(outfitList)
                            .message(message)
                            .build()
            );

            if (index == ConstantString.MAXIMUM_STYLE_FOR_FREE_USER) break;
        }
        return aiStylishResponses;
    }

    public List<List<ClothesResponse>> selectUniqueOutfits(List<ClothesResponse> topInsideClothes,
                                                           List<ClothesResponse> topOutsideClothes,
                                                           List<ClothesResponse> bottomClothes,
                                                           List<ClothesResponse> shoesClothes,
                                                           List<ClothesResponse> accessoriesClothes,
                                                           String ruleTopInsideColor,
                                                           String ruleTopOutsideColor,
                                                           String ruleBottomColor,
                                                           String ruleShoesColor,
                                                           String styleName,
                                                           String bodyShapeName,
                                                           String userID,
                                                           String subRole,
                                                           String choice,
                                                           int numberOfOutfits,
                                                           UserResponseStylish userResponseStylish) throws CustomException {

        Integer maxOutFitCanGenerate = calculateMaxOutfitsCanGenerate(topInsideClothes, topOutsideClothes, bottomClothes, shoesClothes, accessoriesClothes);
        logger.error("This is size of maxOutFitCanGenerate : {}", maxOutFitCanGenerate);
        Integer outfitInMemoryDB = 0;
        List<List<ClothesResponse>> selectedOutfits = new ArrayList<>();
        outfitInMemoryDB = memoryEntityService.countNumberOfOutfitsBaseOnStyleBodyShapeUserID(styleName, bodyShapeName, "," + userID + ",");
        System.out.println(styleName + " " + bodyShapeName + " " + maxOutFitCanGenerate);
        System.out.println(styleName + " " + bodyShapeName + " " + outfitInMemoryDB);
        // Check User is Premium User or Free User
        // If User is Premium Check User choose Suggest Old Outfits until New Outfits arrive
        if (subRole.equals(ESubRole.LV2.toString()) && outfitInMemoryDB.equals(maxOutFitCanGenerate)) {
            List<MemoryEntity> memoryEntityList = memoryEntityService.getAllMemoryEntityByStyleBodyShapeUserAcceptOldOutfit(styleName, bodyShapeName, userID);
            logger.warn("The size of memoryEntityList is {}", memoryEntityList.size());
            if (memoryEntityList.size() > 0 && memoryEntityList.size() == maxOutFitCanGenerate) {
                while (selectedOutfits.size() < numberOfOutfits) {
                    List<ClothesResponse> outfit = new ArrayList<>();
                    String topInsideID = null, topOutsideID = null, bottomKindID = null, shoesTypeID = null, accessoryKindID = null;
                    MemoryEntity memoryEntity = selectRandomMemoryEntity(memoryEntityList);

                    if (checkStringIsNotEmptyOrBlank(memoryEntity.getTopInsideID())) {
//                        ClothesResponse topInside = clothesService.getClothesByID(Integer.parseInt(memoryEntity.getTopInsideID()));
                        ClothesResponse topInside = clothesDataService.getClothesResponseList().get(Integer.parseInt(memoryEntity.getTopInsideID()));
                        topInsideID = topInside.getClothesID().toString();
                        topInside.setUserResponseStylish(userResponseStylish);
                        outfit.add(topInside);
                    }

                    if (checkStringIsNotEmptyOrBlank(memoryEntity.getTopOutsideID())) {
//                        ClothesResponse topOutside = clothesService.getClothesByID(Integer.parseInt(memoryEntity.getTopOutsideID()));
                        ClothesResponse topOutside = clothesDataService.getClothesResponseList().get(Integer.parseInt(memoryEntity.getTopOutsideID()));
                        topInsideID = topOutside.getClothesID().toString();
                        topOutside.setUserResponseStylish(userResponseStylish);
                        outfit.add(topOutside);
                    }

                    if (checkStringIsNotEmptyOrBlank(memoryEntity.getBottomKindID())) {
                        ClothesResponse bottomKind = clothesDataService.getClothesResponseList().get(Integer.parseInt(memoryEntity.getBottomKindID()));
                        bottomKindID = bottomKind.getClothesID().toString();
                        bottomKind.setUserResponseStylish(userResponseStylish);
                        outfit.add(bottomKind);
                    }

                    if (checkStringIsNotEmptyOrBlank(memoryEntity.getShoesTypeID())) {
                        ClothesResponse shoesType = clothesDataService.getClothesResponseList().get(Integer.parseInt(memoryEntity.getShoesTypeID()));
                        shoesTypeID = shoesType.getClothesID().toString();
                        shoesType.setUserResponseStylish(userResponseStylish);
                        outfit.add(shoesType);
                    }

                    if (checkStringIsNotEmptyOrBlank(memoryEntity.getAccessoryKindID())) {
                        ClothesResponse accessoriesKind = clothesDataService.getClothesResponseList().get(Integer.parseInt(memoryEntity.getAccessoryKindID()));
                        accessoryKindID = accessoriesKind.getClothesID().toString();
                        accessoriesKind.setUserResponseStylish(userResponseStylish);
                        outfit.add(accessoriesKind);
                    }
                    selectedOutfits.add(outfit);
                }
            }
            return selectedOutfits;
        }
        logger.info("Inside Method 2 When User not choose Old Outfits");
        while (selectedOutfits.size() < numberOfOutfits) {

            List<ClothesResponse> outfit = new ArrayList<>();
            String topInsideID = null, topOutsideID = null, bottomKindID = null, shoesTypeID = null, accessoryKindID = null;
//            outfitInMemoryDB = memoryEntityService.countNumberOfOutfitsBaseOnStyleBodyShapeUserID(styleName, bodyShapeName, "," + userID + ",");
            // Outfit for User store in DB can not greater than max Outfit can Generate by Rule Matching Clothes
            if (outfitInMemoryDB == maxOutFitCanGenerate) break;

            if (topInsideClothes.size() > 0) {
                ClothesResponse topInside = selectRandomElement(topInsideClothes);
                topInsideID = topInside.getClothesID().toString();
                topInside.setUserResponseStylish(userResponseStylish);
                outfit.add(topInside);
            }
            if (topOutsideClothes.size() > 0) {
                ClothesResponse topOutside = selectRandomElement(topOutsideClothes);
                topOutsideID = topOutside.getClothesID().toString();
                topOutside.setUserResponseStylish(userResponseStylish);
                outfit.add(topOutside);
            }
            if (bottomClothes.size() > 0) {
                ClothesResponse bottomKind = selectRandomElement(bottomClothes);
                bottomKindID = bottomKind.getClothesID().toString();
                bottomKind.setUserResponseStylish(userResponseStylish);
                outfit.add(bottomKind);
            }
            if (shoesClothes.size() > 0) {
                ClothesResponse shoesType = selectRandomElement(shoesClothes);
                shoesTypeID = shoesType.getClothesID().toString();
                shoesType.setUserResponseStylish(userResponseStylish);
                outfit.add(shoesType);
            }
            if (accessoriesClothes.size() > 0) {
                ClothesResponse accessoryKind = selectRandomElement(accessoriesClothes);
                accessoryKindID = accessoryKind.getClothesID().toString();
                accessoryKind.setUserResponseStylish(userResponseStylish);
                outfit.add(accessoryKind);
            }

            MemoryRequest memoryRequest = MemoryRequest
                    .builder()
                    .styleName(styleName)
                    .bodyShapeName(bodyShapeName)
                    .topInsideID(topInsideID)
                    .topInsideColor(ruleTopInsideColor)
                    .topOutsideID(topOutsideID)
                    .topOutsideColor(ruleTopOutsideColor)
                    .bottomKindID(bottomKindID)
                    .bottomColor(ruleBottomColor)
                    .shoesTypeID(shoesTypeID)
                    .shoesTypeColor(ruleShoesColor)
                    .accessoryKindID(accessoryKindID)
                    .build();

            var memoryEntity = memoryEntityService.getMemoryByMemoryRequest(memoryRequest);
            if (memoryEntity != null) {
                logger.error("Memory Entity is Existed {}", memoryEntity);
            }

            // Check Outfit is existed or not when generate random outfits
            // Check Outfit which is dislike or suggest to that User or not
            if (!containsOutfit(selectedOutfits, outfit)) {
                if (memoryEntity == null) {
                    memoryRequest.setSuggestClothesToUser("," + userID + ",");
                    memoryEntityService.createMemoryEntity(memoryRequest);
                } else {
                    if (memoryEntity.getDislikeClothesByUser() != null && memoryEntity.getDislikeClothesByUser().contains("," + userID + ",")) {
                        continue;
                    }
                    if (memoryEntity.getSuggestClothesToUser() != null && memoryEntity.getSuggestClothesToUser().contains("," + userID + ",")) {
                        continue;
                    }
                    memoryEntityService.updateMemoryEntityForDislikeAndSuggest(memoryEntity.getMemoryID(), userID + ",", "SUGGEST");
                }
                selectedOutfits.add(outfit);
                outfitInMemoryDB++; // Successful Choose outfit which doesn't appear to UserID
            }
        }

        return selectedOutfits;
    }

    private Integer calculateMaxOutfitsCanGenerate(List<ClothesResponse> topInsideClothes,
                                                   List<ClothesResponse> topOutsideClothes,
                                                   List<ClothesResponse> bottomClothes,
                                                   List<ClothesResponse> shoesClothes,
                                                   List<ClothesResponse> accessoriesClothes) {
        Integer value = 1;

        if (topInsideClothes.size() > 0) value *= topInsideClothes.size();

        if (topOutsideClothes.size() > 0) value *= topOutsideClothes.size();

        if (bottomClothes.size() > 0) value *= bottomClothes.size();

        if (shoesClothes.size() > 0) value *= shoesClothes.size();

        if (accessoriesClothes.size() > 0) value *= accessoriesClothes.size();

        return value;
    }

    private boolean checkStringIsNotEmptyOrBlank(String str) {
        return str != null;
    }


    private MemoryEntity selectRandomMemoryEntity(List<MemoryEntity> memoryEntityList) {
        return memoryEntityList.get(random.nextInt(memoryEntityList.size()));
    }

    private ClothesResponse selectRandomElement(List<ClothesResponse> clothesList) {
        return clothesList.get(random.nextInt(clothesList.size()));
    }

    private boolean containsOutfit(List<List<ClothesResponse>> selectedOutfits, List<ClothesResponse> outfit) {
        return selectedOutfits
                .stream()
                .anyMatch(existedOutfit -> isSameOutfit(existedOutfit, outfit));
    }

    private boolean isSameOutfit(List<ClothesResponse> outfit1, List<ClothesResponse> outfit2) {
        if (outfit1.size() != outfit2.size()) {
            return false;
        }
        for (ClothesResponse item : outfit1) {
            if (!outfit2.contains(item)) {
                return false;
            }
        }
        return true;
    }

    private List<Style> selectRandomPremiumStyle(List<Style> styleList) {
        List<Style> styles = new ArrayList<>();
        while (styles.size() < ConstantString.MAXIMUM_STYLE_FOR_PREMIUM_USER_PER_WEEK) {
            Style style = styleList.get(random.nextInt(styleList.size()));
            if (!styles.contains(style)) styles.add(style);
        }
        return styles;
    }

    private List<Integer> calculateNumberOfStyleAppearDuringWeek(List<Style> styleList) {
        List<Integer> list = new ArrayList<>();
        int days = ConstantString.MAXIMUM_STYLE_FOR_PREMIUM_USER_PER_WEEK;
        int style = styleList.size();
        int remainder = 0;
        for (int i = 0; i < style; ++i) {
            remainder = days / (style - i);
            days -= remainder;
            list.add(remainder);
        }
        return list;
    }

    private List<AIStylishResponse> suggestClothesForPremiumUser(User user,
                                                                 String subRole,
                                                                 BodyShape bodyShape,
                                                                 List<Style> styleList,
                                                                 UserResponseStylish userResponseStylish) throws CustomException {

        List<Style> selectedStyle = null;
        List<Integer> calculateDaysForEachStyle = null;
        if (styleList.size() > ConstantString.MAXIMUM_STYLE_FOR_PREMIUM_USER_PER_WEEK)
            selectedStyle = selectRandomPremiumStyle(styleList);
        else {
            selectedStyle = new ArrayList<>(styleList);
            calculateDaysForEachStyle = calculateNumberOfStyleAppearDuringWeek(styleList);
        }

        List<RuleMatchingClothesResponse> ruleMatchingClothesResponses = selectedStyle
                .stream()
                .map(style -> ruleMatchingClothesService.getRuleMatchingClothesByStyleAndBodyShape(style.getStyleID(), bodyShape.getBodyShapeID()))
                .collect(Collectors.toList());

        String userGender = (user.getGender() == true) ? "MALE" : "FEMALE";

        List<AIStylishResponse> aiStylishResponses = new ArrayList<>();

        for (int i = 0; i < ruleMatchingClothesResponses.size(); ++i) {

            RuleMatchingClothesResponse rule = ruleMatchingClothesResponses.get(i);

            List<List<ClothesResponse>> outfitList = new ArrayList<>();

            List<ClothesResponse> topInsideClothes = clothesDataService.getClothesBaseOnTypeOfClothesAndColorOrMaterials(
                    rule.getTopInside(), rule.getTopInsideColor(), rule.getTopMaterial(), userGender);

//            List<ClothesResponse> topInsideClothes = clothesService.getClothesBaseOnTypeOfClothesAndColorOrMaterials(
//                    rule.getTopInside(), rule.getTopInsideColor(), rule.getTopMaterial());

            for (ClothesResponse clothesResponse : topInsideClothes) {
                logger.warn("This is Top Inside Clothes {}", clothesResponse);
            }

            List<ClothesResponse> topOutsideClothes = clothesDataService.getClothesBaseOnTypeOfClothesAndColorOrMaterials(
                    rule.getTopOutside(), rule.getTopOutsideColor(), rule.getTopMaterial(), userGender);
//            List<ClothesResponse> topOutsideClothes = clothesService.getClothesBaseOnTypeOfClothesAndColorOrMaterials(
//                    rule.getTopOutside(), rule.getTopOutsideColor(), rule.getTopMaterial());

            for (ClothesResponse clothesResponse : topOutsideClothes) {
                logger.warn("This is Top Outside Clothes {}", clothesResponse);
            }

            List<ClothesResponse> bottomClothes = clothesDataService.getClothesBaseOnTypeOfClothesAndColorOrMaterials(
                    rule.getBottomKind(), rule.getBottomColor(), rule.getBottomMaterial(), userGender);

//            List<ClothesResponse> bottomClothes = clothesService.getClothesBaseOnTypeOfClothesAndColorOrMaterials(
//                    rule.getBottomKind(), rule.getBottomColor(), rule.getBottomMaterial());

            for (ClothesResponse clothesResponse : bottomClothes) {
                logger.warn("This is Bottom Clothes {}", clothesResponse);
            }

            List<ClothesResponse> shoesClothes = clothesDataService.getClothesBaseOnTypeOfClothesAndColorOrMaterials(
                    rule.getShoesType(), rule.getShoesTypeColor(), rule.getBottomMaterial(), userGender);

//            List<ClothesResponse> shoesClothes = clothesService.getClothesBaseOnTypeOfClothesAndColorOrMaterials(
//                    rule.getShoesType(), rule.getShoesTypeColor(), rule.getBottomMaterial());

            for (ClothesResponse clothesResponse : shoesClothes) {
                logger.warn("This is Shoes Clothes {}", clothesResponse);
            }

            List<ClothesResponse> accessoriesClothes = clothesDataService.getClothesBaseOnTypeOfClothesAndMaterial(
                    rule.getAccessoryKind(), rule.getAccessoryMaterial(), userGender);
//            List<ClothesResponse> accessoriesClothes = clothesService.getClothesBaseOnTypeOfClothesAndMaterial(
//                    rule.getAccessoryKind(), rule.getAccessoryMaterial());

            for (ClothesResponse clothesResponse : accessoriesClothes) {
                logger.warn("This is Accessories Clothes {}", clothesResponse);
            }

            String message = "";
            if (styleList.size() == ConstantString.PREMIUM_HAVE_ONLY_ONE_STYLE) {
                outfitList = selectUniqueOutfits(
                        topInsideClothes,
                        topOutsideClothes,
                        bottomClothes,
                        shoesClothes,
                        accessoriesClothes,
                        rule.getTopInsideColor(),
                        rule.getTopOutsideColor(),
                        rule.getBottomColor(),
                        rule.getShoesTypeColor(),
                        rule.getStyleName(),
                        rule.getBodyShapeName(),
                        user.getUserID(),
                        subRole,
                        ConstantString.SUGGEST_OUTFITS_FOR_USER,
                        ConstantString.SUGGEST_CLOTHES_FOR_PREMIUM_USER_HAVING_ONE_STYLE,
                        userResponseStylish);

                if (outfitList.size() == ConstantString.SUGGEST_CLOTHES_FOR_PREMIUM_USER_HAVING_ONE_STYLE) {
                    message = ConstantMessage.SUGGEST_FULL_CLOTHES_FOR_PREMIUM_USER.getMessage();
                } else message = ConstantMessage.SUGGEST_MISSING_OR_RUN_OUT_OF_CLOTHES_FOR_PREMIUM_USER.getMessage();
            } else if (styleList.size() > ConstantString.MAXIMUM_STYLE_FOR_PREMIUM_USER_PER_WEEK) {
                List<List<ClothesResponse>> outfits = selectUniqueOutfits(
                        topInsideClothes,
                        topOutsideClothes,
                        bottomClothes,
                        shoesClothes,
                        accessoriesClothes,
                        rule.getTopInsideColor(),
                        rule.getTopOutsideColor(),
                        rule.getBottomColor(),
                        rule.getShoesTypeColor(),
                        rule.getStyleName(),
                        rule.getBodyShapeName(),
                        user.getUserID(),
                        subRole,
                        ConstantString.SUGGEST_OUTFITS_FOR_USER,
                        ConstantString.SUGGEST_CLOTHES_FOR_PREMIUM_USER_A_DAY,
                        userResponseStylish);

                for (List<ClothesResponse> out : outfits) {
                    if (!containsOutfit(outfitList, out)) {
                        outfitList.add(out);
                    }
                }

                for (List<ClothesResponse> outfit : outfitList) {
                    logger.error("This is Outfit: ");
                    for (ClothesResponse clothes : outfit) {
                        logger.warn("This is clothes from Outfit {}", clothes);
                    }
                }
                if (outfitList.size() == ConstantString.SUGGEST_CLOTHES_FOR_PREMIUM_USER_A_DAY) {
                    message = ConstantMessage.SUGGEST_FULL_CLOTHES_FOR_PREMIUM_USER.getMessage();
                } else message = ConstantMessage.SUGGEST_MISSING_OR_RUN_OUT_OF_CLOTHES_FOR_PREMIUM_USER.getMessage();
            } else {
                int index = 0;
                for (int j = 0; j < calculateDaysForEachStyle.get(i); j++) {
                    List<List<ClothesResponse>> outfits = selectUniqueOutfits(
                            topInsideClothes,
                            topOutsideClothes,
                            bottomClothes,
                            shoesClothes,
                            accessoriesClothes,
                            rule.getTopInsideColor(),
                            rule.getTopOutsideColor(),
                            rule.getBottomColor(),
                            rule.getShoesTypeColor(),
                            rule.getStyleName(),
                            rule.getBodyShapeName(),
                            user.getUserID(),
                            subRole,
                            ConstantString.SUGGEST_OUTFITS_FOR_USER,
                            ConstantString.SUGGEST_CLOTHES_FOR_PREMIUM_USER_A_DAY,
                            userResponseStylish);

                    index += outfits.size();

                    for (List<ClothesResponse> out : outfits) {
                        if (!containsOutfit(outfitList, out)) {
                            outfitList.add(out);
                        }
                    }
                }

                if (index == ConstantString.SUGGEST_CLOTHES_FOR_PREMIUM_USER_A_DAY * calculateDaysForEachStyle.get(i)) {
                    message = ConstantMessage.SUGGEST_FULL_CLOTHES_FOR_PREMIUM_USER.getMessage();
                } else message = ConstantMessage.SUGGEST_MISSING_OR_RUN_OUT_OF_CLOTHES_FOR_PREMIUM_USER.getMessage();

                for (List<ClothesResponse> outfit : outfitList) {
                    logger.error("This is Outfit: ");
                    for (ClothesResponse clothes : outfit) {
                        logger.warn("This is clothes from Outfit {}", clothes);
                    }
                }
            }

            aiStylishResponses.add(
                    AIStylishResponse
                            .builder()
                            .styleName(rule.getStyleName())
                            .bodyShapeName(rule.getBodyShapeName())
                            .outfits(outfitList)
                            .message(message)
                            .build()
            );
        }
        return aiStylishResponses;
    }

    @Override
    public AIStylishResponse createNewClothesAfterRejectClothesForPremiumUser(RejectClothesRequest rejectClothesRequest) throws CustomException {
        Style style = styleService.getStyleByStyleName(rejectClothesRequest.getStyleName());
        if (style == null) {
            throw new CustomException(ConstantMessage.STYLE_NAME_IS_NOT_EXISTED.getMessage());
        }

        BodyShape bodyShape = bodyShapeService.getBodyShapeByBodyShapeName(rejectClothesRequest.getBodyShapeName());
        if (bodyShape == null) {
            throw new CustomException(ConstantMessage.BODY_SHAPE_NAME_IS_NOT_EXISTED.getMessage());
        }

        // Check ID User or User Entity is exist or not
        Optional.of(rejectClothesRequest.getUserID())
                .filter(id -> !id.isEmpty() && !id.isBlank())
                .orElseThrow(() -> new CustomException(ConstantMessage.USERID_IS_EMPTY_OR_NOT_EXIST.getMessage()));

        User user = Optional.ofNullable(userService.getUserEntityByUserID(rejectClothesRequest.getUserID()))
                .orElseThrow(() -> new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage()));

        Customer customer = customerService.getCustomerByID(rejectClothesRequest.getUserID());
        String subRole = subroleService.getSubroleByID(customer.getSubRoleID()).getSubRoleName().toString();

        String userGender = (user.getGender() == true) ? "MALE" : "FEMALE";

        UserResponseStylish userResponseStylish = UserResponseStylish
                .builder()
                .userID(user.getUserID())
                .username(user.getUsername())
                .imgUrl(user.getImgUrl())
                .build();
        RuleMatchingClothesResponse rule = ruleMatchingClothesService.getRuleMatchingClothesByStyleAndBodyShape(style.getStyleID(), bodyShape.getBodyShapeID());

        List<List<ClothesResponse>> outfitList = new ArrayList<>();

        List<ClothesResponse> topInsideClothes = clothesDataService.getClothesBaseOnTypeOfClothesAndColorOrMaterials(
                rule.getTopInside(), rule.getTopInsideColor(), rule.getTopMaterial(), userGender);

//            List<ClothesResponse> topInsideClothes = clothesService.getClothesBaseOnTypeOfClothesAndColorOrMaterials(
//                    rule.getTopInside(), rule.getTopInsideColor(), rule.getTopMaterial());

        for (ClothesResponse clothesResponse : topInsideClothes) {
            logger.warn("This is Top Inside Clothes {}", clothesResponse);
        }

        List<ClothesResponse> topOutsideClothes = clothesDataService.getClothesBaseOnTypeOfClothesAndColorOrMaterials(
                rule.getTopOutside(), rule.getTopOutsideColor(), rule.getTopMaterial(), userGender);
//            List<ClothesResponse> topOutsideClothes = clothesService.getClothesBaseOnTypeOfClothesAndColorOrMaterials(
//                    rule.getTopOutside(), rule.getTopOutsideColor(), rule.getTopMaterial());

        for (ClothesResponse clothesResponse : topOutsideClothes) {
            logger.warn("This is Top Outside Clothes {}", clothesResponse);
        }

        List<ClothesResponse> bottomClothes = clothesDataService.getClothesBaseOnTypeOfClothesAndColorOrMaterials(
                rule.getBottomKind(), rule.getBottomColor(), rule.getBottomMaterial(), userGender);

//            List<ClothesResponse> bottomClothes = clothesService.getClothesBaseOnTypeOfClothesAndColorOrMaterials(
//                    rule.getBottomKind(), rule.getBottomColor(), rule.getBottomMaterial());

        for (ClothesResponse clothesResponse : bottomClothes) {
            logger.warn("This is Bottom Clothes {}", clothesResponse);
        }

        List<ClothesResponse> shoesClothes = clothesDataService.getClothesBaseOnTypeOfClothesAndColorOrMaterials(
                rule.getShoesType(), rule.getShoesTypeColor(), rule.getBottomMaterial(), userGender);

//            List<ClothesResponse> shoesClothes = clothesService.getClothesBaseOnTypeOfClothesAndColorOrMaterials(
//                    rule.getShoesType(), rule.getShoesTypeColor(), rule.getBottomMaterial());

        for (ClothesResponse clothesResponse : shoesClothes) {
            logger.warn("This is Shoes Clothes {}", clothesResponse);
        }

        List<ClothesResponse> accessoriesClothes = clothesDataService.getClothesBaseOnTypeOfClothesAndMaterial(
                rule.getAccessoryKind(), rule.getAccessoryMaterial(), userGender);
//            List<ClothesResponse> accessoriesClothes = clothesService.getClothesBaseOnTypeOfClothesAndMaterial(
//                    rule.getAccessoryKind(), rule.getAccessoryMaterial());

        for (ClothesResponse clothesResponse : accessoriesClothes) {
            logger.warn("This is Accessories Clothes {}", clothesResponse);
        }

        MemoryEntity memoryEntity = memoryEntityService.getMemoryForRejectClothesRequest(rejectClothesRequest);
        List<MemoryEntity> outfitInMemoryDB = memoryEntityService.getAllMemoryEntityByStyleBodyShapeUserAcceptOldOutfit(rejectClothesRequest.getStyleName(), rejectClothesRequest.getBodyShapeName(), "," + rejectClothesRequest.getUserID() + ",");

        // Condition if only use for Users don't want to suggest old Outfits
        if (outfitInMemoryDB.size() == 0) {
            if (memoryEntity == null) {
                throw new CustomException("This Outfits is not existed.");
            }

            if (memoryEntity.getDislikeClothesByUser() != null && memoryEntity.getDislikeClothesByUser().contains("," + rejectClothesRequest.getUserID() + ",")) {
                throw new CustomException("Dislike Clothes is already suggest to Customer");
            }
        }

        outfitList = selectUniqueOutfits(
                topInsideClothes,
                topOutsideClothes,
                bottomClothes,
                shoesClothes,
                accessoriesClothes,
                rule.getTopInsideColor(),
                rule.getTopOutsideColor(),
                rule.getBottomColor(),
                rule.getShoesTypeColor(),
                rule.getStyleName(),
                rule.getBodyShapeName(),
                rejectClothesRequest.getUserID(),
                subRole,
                ConstantString.RENEW_OUTFITS_FOR_PREMIUM,
                ConstantString.SUGGEST_CLOTHES_FOR_PREMIUM_USER_AFTER_REJECT,
                userResponseStylish);

        if (outfitList.size() > 0 && outfitInMemoryDB.size() == 0)
            memoryEntityService.updateMemoryEntityForDislikeAndSuggest(memoryEntity.getMemoryID(), rejectClothesRequest.getUserID() + ",", "DISLIKE");

        String message = "";
        if (outfitList.size() == ConstantString.SUGGEST_CLOTHES_FOR_PREMIUM_USER_AFTER_REJECT) {
            message = ConstantMessage.SUGGEST_FULL_CLOTHES_FOR_PREMIUM_USER.getMessage();
        } else message = ConstantMessage.SUGGEST_MISSING_OR_RUN_OUT_OF_CLOTHES_FOR_PREMIUM_USER.getMessage();

        return AIStylishResponse
                .builder()
                .styleName(rule.getStyleName())
                .bodyShapeName(rule.getBodyShapeName())
                .outfits(outfitList)
                .message(message)
                .build();
    }

    @Override
    public List<AIStylishResponse> selectChoiceWhenRunOutOfOutfitsForPremium(SuggestChoiceForPremiumUser suggestChoiceForPremiumUser) throws CustomException {
        Style style = styleService.getStyleByStyleName(suggestChoiceForPremiumUser.getStyleName());
        if (style == null) {
            throw new CustomException(ConstantMessage.STYLE_NAME_IS_NOT_EXISTED.getMessage());
        }

        BodyShape bodyShape = bodyShapeService.getBodyShapeByBodyShapeName(suggestChoiceForPremiumUser.getBodyShapeName());
        if (bodyShape == null) {
            throw new CustomException(ConstantMessage.BODY_SHAPE_NAME_IS_NOT_EXISTED.getMessage());
        }

        // Check ID User or User Entity is exist or not
        Optional.of(suggestChoiceForPremiumUser.getUserID())
                .filter(id -> !id.isEmpty() && !id.isBlank())
                .orElseThrow(() -> new CustomException(ConstantMessage.USERID_IS_EMPTY_OR_NOT_EXIST.getMessage()));

        User user = Optional.ofNullable(userService.getUserEntityByUserID(suggestChoiceForPremiumUser.getUserID()))
                .orElseThrow(() -> new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage()));

        Customer customer = customerService.getCustomerByID(suggestChoiceForPremiumUser.getUserID());
        String subRole = subroleService.getSubroleByID(customer.getSubRoleID()).getSubRoleName().toString();

        if(!subRole.equals(ESubRole.LV2.toString()))
        {
            throw new CustomException(ConstantString.SELECT_CHOICE_WHEN_RUN_OUT_OF_OUTFITS_ONLY_FOR_PREMIUM);
        }

        if (suggestChoiceForPremiumUser.getChoice().toUpperCase().replace(' ', '_').equals(ConstantString.SUGGEST_OLD_OUTFITS_UNTIL_NEW_OUTFITS_ARRIVE)) {

            memoryEntityService.updateAcceptOldOutfitsUntilNewOutfitArrive(suggestChoiceForPremiumUser.getStyleName(), suggestChoiceForPremiumUser.getBodyShapeName(), suggestChoiceForPremiumUser.getUserID() + ",");
        }
        return new ArrayList<>();
    }

    @Override
    public CalculateOutfitsResponse calculateMaximumOutfitsCanGenerate(CalculateOutfitsRequest calculateOutfitsRequest) throws CustomException {
        Style style = styleService.getStyleByStyleName(calculateOutfitsRequest.getStyleName());
        if (style == null) {
            throw new CustomException(ConstantMessage.STYLE_NAME_IS_NOT_EXISTED.getMessage());
        }

        BodyShape bodyShape = bodyShapeService.getBodyShapeByBodyShapeName(calculateOutfitsRequest.getBodyShapeName());
        if (bodyShape == null) {
            throw new CustomException(ConstantMessage.BODY_SHAPE_NAME_IS_NOT_EXISTED.getMessage());
        }

        RuleMatchingClothesResponse rule = ruleMatchingClothesService.getRuleMatchingClothesByStyleAndBodyShape(style.getStyleID(), bodyShape.getBodyShapeID());

        String userGender = calculateOutfitsRequest.getGenderType();

        List<ClothesResponse> topInsideClothes = clothesDataService.getClothesBaseOnTypeOfClothesAndColorOrMaterials(
                rule.getTopInside(), rule.getTopInsideColor(), rule.getTopMaterial(), userGender);


        for (ClothesResponse clothesResponse : topInsideClothes) {
            logger.warn("This is Top Inside Clothes {}", clothesResponse);
        }

        List<ClothesResponse> topOutsideClothes = clothesDataService.getClothesBaseOnTypeOfClothesAndColorOrMaterials(
                rule.getTopOutside(), rule.getTopOutsideColor(), rule.getTopMaterial(), userGender);

        for (ClothesResponse clothesResponse : topOutsideClothes) {
            logger.warn("This is Top Outside Clothes {}", clothesResponse);
        }

        List<ClothesResponse> bottomClothes = clothesDataService.getClothesBaseOnTypeOfClothesAndColorOrMaterials(
                rule.getBottomKind(), rule.getBottomColor(), rule.getBottomMaterial(), userGender);


        for (ClothesResponse clothesResponse : bottomClothes) {
            logger.warn("This is Bottom Clothes {}", clothesResponse);
        }

        List<ClothesResponse> shoesClothes = clothesDataService.getClothesBaseOnTypeOfClothesAndColorOrMaterials(
                rule.getShoesType(), rule.getShoesTypeColor(), rule.getBottomMaterial(), userGender);


        for (ClothesResponse clothesResponse : shoesClothes) {
            logger.warn("This is Shoes Clothes {}", clothesResponse);
        }

        List<ClothesResponse> accessoriesClothes = clothesDataService.getClothesBaseOnTypeOfClothesAndMaterial(
                rule.getAccessoryKind(), rule.getAccessoryMaterial(), userGender);

        for (ClothesResponse clothesResponse : accessoriesClothes) {
            logger.warn("This is Accessories Clothes {}", clothesResponse);
        }

        Integer maxOutFitCanGenerate = calculateMaxOutfitsCanGenerate(topInsideClothes, topOutsideClothes, bottomClothes, shoesClothes, accessoriesClothes);

        return CalculateOutfitsResponse
                .builder()
                .styleName(calculateOutfitsRequest.getStyleName())
                .bodyShapeName(calculateOutfitsRequest.getBodyShapeName())
                .genderType(calculateOutfitsRequest.getGenderType())
                .maximumOutfits(maxOutFitCanGenerate)
                .build();
    }
}
