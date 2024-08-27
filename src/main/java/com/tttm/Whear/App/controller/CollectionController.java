package com.tttm.Whear.App.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tttm.Whear.App.constant.APIConstant.CollectionAPI;
import com.tttm.Whear.App.service.CollectionService;
import com.tttm.Whear.App.utils.request.CollectionRequest;
import com.tttm.Whear.App.utils.response.CollectionResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(CollectionAPI.COLLECTION)
public class CollectionController {

  @Autowired
  CollectionService collectionService;

  @GetMapping(CollectionAPI.GET_ALL_COLLECTION_BY_USER_ID)
  public ObjectNode getAllCollectionByUserID(@RequestParam(name = "user_id") String userID) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      List<CollectionResponse> collectionResponseList = collectionService.getCollectionsOfUser(
          userID);
      if (collectionResponseList == null || collectionResponseList.isEmpty()) {
        ObjectNode respon = objectMapper.createObjectNode();
        respon.put("success", 200);
        respon.put("message", "Their is no collection for user: " + userID);
        respon.set("data", objectMapper.valueToTree(collectionResponseList));
        return respon;
      } else {
        ObjectNode respon = objectMapper.createObjectNode();
        respon.put("success", 200);
        respon.put("message",
            "Their are " + collectionResponseList.size() + " collections for user: " + userID);
        ArrayNode arrayNode = objectMapper.valueToTree(collectionResponseList);
        respon.set("data", arrayNode);
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

  @GetMapping(CollectionAPI.GET_COLLECTION_BY_ID)
  public ObjectNode getCollectionByID(@RequestParam(name = "collection_id") Integer collectionID) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      ObjectNode response = objectMapper.createObjectNode();
      CollectionResponse collectionResponse = collectionService.getCollectionByCollectionID(
          collectionID);
      if (collectionResponse != null) {
        response.put("success", 200);
        response.put("message", "Collection is getted!");
        response.set("data", objectMapper.valueToTree(collectionResponse));
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

  @PutMapping(CollectionAPI.UPDATE_COLLECTION_BY_ID)
  public ObjectNode updateCollectionByID(@RequestBody CollectionRequest newCollection) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      ObjectNode response = objectMapper.createObjectNode();
      CollectionResponse collectionResponse = collectionService.updateCollectionByID(newCollection);
      if (collectionResponse != null) {
        response.put("success", 200);
        response.put("message", "Collection is updated!");
        response.set("data", objectMapper.valueToTree(collectionResponse));
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

  @DeleteMapping(CollectionAPI.DELETE_COLLECTION_BY_ID)
  public ObjectNode deleteCollectionByID(
      @RequestParam(name = "collection_id") Integer collectionID) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      ObjectNode response = objectMapper.createObjectNode();
      collectionService.deleteCollectionByID(collectionID);
//      if(collectionResponse != null){
      response.put("success", 200);
      response.put("message", "Collection is deleted!");
      response.set("data", null);
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

  @PostMapping(CollectionAPI.CREATE_COLLECTION)
  public ObjectNode createCollection(@RequestBody CollectionRequest collection) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      ObjectNode response = objectMapper.createObjectNode();
      CollectionResponse collectionResponse = collectionService.createCollection(collection);
      if (collectionResponse != null) {
        response.put("success", 200);
        response.put("message", "Collection is created!");
        response.set("data", objectMapper.valueToTree(collectionResponse));
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
}
