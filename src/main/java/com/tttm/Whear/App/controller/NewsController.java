package com.tttm.Whear.App.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tttm.Whear.App.constant.APIConstant;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.service.NewsService;
import com.tttm.Whear.App.utils.request.NewsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(APIConstant.NewsAPI.NEWS)
public class NewsController {
    private final NewsService newsService;

    @PostMapping(APIConstant.NewsAPI.CREATE_NEWS)
    public ObjectNode createNews(@RequestBody NewsRequest newsRequest) throws CustomException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("success", 200);
            respon.put("message", "Create News Successfully");
            respon.set("data", objectMapper.valueToTree(newsService.createNews(newsRequest)));
            return respon;
        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }

    @PutMapping(APIConstant.NewsAPI.UPDATE_NEWS)
    public ObjectNode updateNews(@RequestBody NewsRequest newsRequest) throws CustomException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("success", 200);
            respon.put("message", "Update News Successfully");
            respon.set("data", objectMapper.valueToTree(newsService.updateNews(newsRequest)));
            return respon;
        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }

    @DeleteMapping(APIConstant.NewsAPI.DELETE_NEWS)
    public ObjectNode deleteNews(@RequestBody NewsRequest newsRequest) throws CustomException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            newsService.deleteNews(newsRequest);
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("success", 200);
            respon.put("message", "Delete News Successfully");
            respon.put("data", "[]");
            return respon;
        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }

    @GetMapping(APIConstant.NewsAPI.GET_NEWS_BY_ID)
    public ObjectNode getNewsByID(@RequestParam("newsID") Integer newsID) throws CustomException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("success", 200);
            respon.put("message", "GET News Successfully");
            respon.put("data", objectMapper.valueToTree(newsService.getNewsByID(newsID)));
            return respon;
        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }

    @GetMapping(APIConstant.NewsAPI.GET_ALL_NEWS)
    public ObjectNode getAllNews() throws CustomException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("success", 200);
            respon.put("message", "GET News Successfully");
            respon.put("data", objectMapper.valueToTree(newsService.getAllNews()));
            return respon;
        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }

    @GetMapping(APIConstant.NewsAPI.GET_NEWS_BY_BRAND_ID)
    public ObjectNode getNewsByBrandID(@RequestParam("brandID") Integer brandID) throws CustomException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("success", 200);
            respon.put("message", "GET News Successfully");
            respon.put("data", objectMapper.valueToTree(newsService.getNewsByBrandID(brandID)));
            return respon;
        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }

    @GetMapping(APIConstant.NewsAPI.GET_NEWS_BY_TYPE_OF_NEWS)
    public ObjectNode getNewsByTypeOfNews(@RequestBody NewsRequest newsRequest) throws CustomException {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("success", 200);
            respon.put("message", "GET News Successfully");
            respon.put("data", objectMapper.valueToTree(newsService.getNewsByTypeOfNews(newsRequest)));
            return respon;
        } catch (Exception ex) {
            ObjectNode respon = objectMapper.createObjectNode();
            respon.put("error", -1);
            respon.put("message", ex.getMessage());
            respon.set("data", null);
            return respon;
        }
    }
}
