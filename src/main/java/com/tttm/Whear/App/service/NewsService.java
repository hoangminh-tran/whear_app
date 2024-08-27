package com.tttm.Whear.App.service;

import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.utils.request.NewsRequest;
import com.tttm.Whear.App.utils.response.NewsResponse;

import java.util.List;

public interface NewsService {

    NewsResponse createNews(NewsRequest newsRequest) throws CustomException;

    NewsResponse updateNews(NewsRequest newsRequest) throws CustomException;

    void deleteNews(NewsRequest newsRequest) throws CustomException;

    NewsResponse getNewsByID(Integer newsID) throws CustomException;

    List<NewsResponse> getAllNews() throws CustomException;

    List<NewsResponse> getNewsByBrandID(Integer brandID) throws CustomException;

    List<NewsResponse> getNewsByTypeOfNews(NewsRequest newsRequest) throws CustomException;
}
