package com.tttm.Whear.App.service;

import com.tttm.Whear.App.entity.NewsImages;
import com.tttm.Whear.App.exception.CustomException;

import java.util.List;

public interface NewsImageService {
    void createNewImages(Integer newsID, String image) throws CustomException;

    void deleteNewsImages(Integer newsID) throws CustomException;

    List<NewsImages> getNewsImagesByNewsID(Integer newsID) throws CustomException;
}
