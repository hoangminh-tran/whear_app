package com.tttm.Whear.App.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tttm.Whear.App.constant.APIConstant.ClothesAPI;
import com.tttm.Whear.App.dto.ClothesItemDto;
import com.tttm.Whear.App.dto.Pairs;
import com.tttm.Whear.App.enums.ENotificationAction;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.service.*;
import com.tttm.Whear.App.service.impl.ClothesDataService;
import com.tttm.Whear.App.utils.request.ClothesCollectionRequest;
import com.tttm.Whear.App.utils.request.ClothesRequest;
import com.tttm.Whear.App.utils.request.NotificationRequest;
import com.tttm.Whear.App.utils.request.ReactRequest;
import com.tttm.Whear.App.utils.response.ClothesResponse;
import com.tttm.Whear.App.utils.response.CollectionResponse;
import com.tttm.Whear.App.utils.response.NotificationResponse;
import com.tttm.Whear.App.utils.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(ClothesAPI.CLOTHES)
public class ClothesController {

    private final RecommendationService recommendationService;
    private final ClothesCollectionService clothesCollectionService;
    private final CollectionService collectionService;
    private final ClothesService clothesService;
    private final FollowService followService;
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;
    private final ReactService reactService;
    private final ClothesDataService clothesDataService;
    private final UserService userService;
    private final PostService postService;

    @GetMapping(ClothesAPI.GET_ALL_CLOTHES)
    public ObjectNode getAllClothes() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<ClothesResponse> responseNonNullList = new ArrayList<>();
//            List<ClothesResponse> responseList = WhearAppApplication.getClothesResponseList();
            List<ClothesResponse> responseList = clothesDataService.getClothesResponseList();
            for (int i = 67; i < responseList.size(); i++) {
                responseNonNullList.add(responseList.get(i));
            }
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("success", 200);
            respon.put("message", "Their are " + responseNonNullList.size() + " clothes");
            ArrayNode arrayNode = objectMapper.valueToTree(responseNonNullList);
            respon.set("data", arrayNode);
            return respon;
        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }

    @GetMapping(ClothesAPI.GET_CLOTHES_BY_TYPE_OF_CLOTHES)
    public ObjectNode getClothesByType(@RequestParam("typeOfClothes") String typeOfClothes) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<ClothesResponse> responseNonNullList = new ArrayList<>();
            List<ClothesResponse> responseList = clothesDataService.getClothesResponseList();
            for (int i = 67; i < responseList.size(); i++) {
                if (responseList.get(i).getTypeOfClothes().equals(typeOfClothes)) {
                    responseNonNullList.add(responseList.get(i));
                }
            }
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("success", 200);
            respon.put("message", "Their are " + responseNonNullList.size() + " clothes");
            ArrayNode arrayNode = objectMapper.valueToTree(responseNonNullList);
            respon.set("data", arrayNode);
            return respon;
        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }

    @GetMapping(ClothesAPI.GET_CLOTHES_BY_STYLE)
    public ObjectNode getClothesByStyle(@RequestParam("style") String style) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<ClothesResponse> responseNonNullList = new ArrayList<>();
            List<ClothesResponse> responseList = clothesDataService.getClothesResponseList();
            for (int i = 67; i < responseList.size(); i++) {
                if (responseList.get(i).getClothesStyles().contains(style)) {
                    responseNonNullList.add(responseList.get(i));
                }
            }
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("success", 200);
            respon.put("message", "Their are " + responseNonNullList.size() + " clothes");
            ArrayNode arrayNode = objectMapper.valueToTree(responseNonNullList);
            respon.set("data", arrayNode);
            return respon;
        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }

    @GetMapping(ClothesAPI.GET_CLOTHES_BY_USERID)
    public ObjectNode getClothesByCreatorId(@RequestParam("userId") String userId) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<ClothesResponse> responseNonNullList = new ArrayList<>();
            List<ClothesResponse> responseList = clothesDataService.getClothesResponseList();
            for (int i = 67; i < responseList.size(); i++) {
                if (responseList.get(i).getUser().getUserID().equals(userId)) {
                    responseNonNullList.add(responseList.get(i));
                }
            }
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("success", 200);
            respon.put("message", "Their are " + responseNonNullList.size() + " clothes");
            ArrayNode arrayNode = objectMapper.valueToTree(responseNonNullList);
            respon.set("data", arrayNode);
            return respon;
        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }

    @PostMapping(ClothesAPI.CREATE_CLOTHES)
    public ObjectNode createClothes(@RequestBody ClothesRequest clothesRequest) throws CustomException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ClothesResponse response = clothesService.createClothes(clothesRequest);
            if (response == null) {
                ObjectNode respon = objectMapper.createObjectNode();
                respon.put("success", 200);
                respon.put("message", "Create Fail!");
                respon.set("data", null);
                return respon;
            } else {
                ObjectNode respon = objectMapper.createObjectNode();
                respon.put("success", 200);
                respon.put("message", "Create Clothes Successfully!");
                JsonNode arrayNode = objectMapper.valueToTree(response);
                respon.set("data", arrayNode);

                List<UserResponse> follwers = followService.getAllFollowerUser(clothesRequest.getUserID());
                if (follwers != null && !follwers.isEmpty() && follwers.size() > 0) {
                    for (UserResponse user : follwers) {
                        NotificationRequest notiRequest = NotificationRequest.builder().action(ENotificationAction.CLOTHES.name()).actionID(response.getClothesID()).baseUserID(clothesRequest.getUserID()).targetUserID(user.getUserID()).dateTime(LocalDateTime.now()).message("New Clothes").status(false).build();
                        NotificationResponse notiresponse = notificationService.sendNotification(notiRequest);
                        notiRequest.setNotiID(notiresponse.getNotiID());
                        messagingTemplate.convertAndSend("/topic/public", notiRequest);
                    }
                }
//                List<ClothesResponse> responseList = WhearAppApplication.getClothesResponseList();
                List<ClothesResponse> responseList = clothesDataService.getClothesResponseList();
                responseList.add(response);
//                WhearAppApplication.setClothesResponseList(responseList);
                clothesDataService.setClothesResponseList(responseList);
                ClothesItemDto clothes = recommendationService.convertToClothesItemDto(response);
                String clotheItems = clothes.getNameOfProduct().toUpperCase() + " " + clothes.getTypeOfClothes() + " " + clothes.getShape() + " " + clothes.getMaterials() + " " + clothes.seasonToString() + " " + clothes.sizeToString() + " " + clothes.colorToString() + " " + clothes.styleToString();
//                List<Pairs> clothesItemList = WhearAppApplication.getClothesItemList();
                List<Pairs> clothesItemList = clothesDataService.getClothesItemList();
                clothesItemList.add(new Pairs(clothes.getClothesID(), clotheItems));
//                WhearAppApplication.setClothesItemList(clothesItemList);
                clothesDataService.setClothesItemList(clothesItemList);
                return respon;
            }
        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }

    @GetMapping(ClothesAPI.GET_CLOTHES_BY_ID)
    public ObjectNode getClothesByID(@RequestParam(name = "clothes_id") Integer clothesID, @RequestParam("based_userid") String userID) throws CustomException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ClothesResponse responseList = clothesService.getClothesByID(clothesID);
            responseList.setReacted(reactService.checkContain(clothesID, userID) != null);
            if (responseList == null) {
                ObjectNode respon = objectMapper.createObjectNode();
                respon.put("success", 200);
                respon.put("message", "Clothes not found!");
                respon.set("data", null);
                return respon;
            } else {
                ObjectNode respon = objectMapper.createObjectNode();
                respon.put("success", 200);
                respon.put("message", "Get Clothes Successfully!");
                respon.set("data", objectMapper.valueToTree(responseList));
                return respon;
            }
        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }

    @PostMapping(ClothesAPI.ADD_CLOTHES_TO_COLLECTION)
    public ObjectNode addClothesToCollection(@RequestBody ClothesCollectionRequest clothesCollectionRequest) throws CustomException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            CollectionResponse responseList = clothesCollectionService.addClothesToCollection(clothesCollectionRequest);

            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("success", 200);
            respon.put("message", "Get Clothes Successfully!");
            /**
             * ADD SUCCESS THEN UPDATE NUMBER OF REACT PER CLOTHES
             */
            CollectionResponse collection = collectionService.getCollectionByCollectionID(clothesCollectionRequest.getCollectionID());
            reactService.unSendReact(ReactRequest.builder().userID(collection.getUserID()).postID(clothesCollectionRequest.getClothesID()).react("ADDED: " + clothesCollectionRequest.getCollectionID()).build());
            responseList = collectionService.getCollectionByCollectionID(clothesCollectionRequest.getCollectionID());
            respon.set("data", objectMapper.valueToTree(responseList));
            return respon;
        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }

    @PutMapping(ClothesAPI.UPDATE_CLOTHES)
    public ObjectNode updateCollectionByID(@RequestBody ClothesRequest clothesRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode response = objectMapper.createObjectNode();
            ClothesResponse clothesResponse = clothesService.updateClothes(clothesRequest);
            List<ClothesResponse> clothesResponses = clothesDataService.getClothesResponseList();
            clothesResponses.set(clothesRequest.getClothesID(), clothesResponse);
            List<Pairs> pairsList = clothesDataService.getClothesItemList();
            ClothesItemDto clothes = recommendationService.convertToClothesItemDto(clothesResponses.get(clothesRequest.getClothesID()));
            String ClotheItems = clothes.getNameOfProduct().toUpperCase() + " " + clothes.getTypeOfClothes() + " " + clothes.getShape() + " " +
                    clothes.getMaterials() + " " + clothes.seasonToString() + " " + clothes.sizeToString() + " " + clothes.colorToString() + " " +
                    clothes.styleToString();
            pairsList.set(clothesRequest.getClothesID() - 67, new Pairs(clothesRequest.getClothesID() - 67, ClotheItems));
            if (clothesResponse != null) {
                response.put("success", 200);
                response.put("message", "Collection is updated!");
                response.set("data", objectMapper.valueToTree(clothesResponse));
            }
            return response;
        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }

    @DeleteMapping(ClothesAPI.DELETE_CLOTHES)
    public ObjectNode deleteCollectionByID(@RequestParam(name = "clothes_id") Integer clothesID) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode response = objectMapper.createObjectNode();
            clothesService.deleteClothesByID(clothesID);
//      if(collectionResponse != null){
            response.put("success", 200);
            response.put("message", "Clothes is deleted!");
            response.set("data", null);
            List<ClothesResponse> clothesResponses = clothesDataService.getClothesResponseList();
            clothesResponses.remove(clothesID);
            List<Pairs> pairsList = clothesDataService.getClothesItemList();
            pairsList.remove(clothesID - 67);
//      }
            return response;
        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }
}
