package com.tttm.Whear.App.service.impl;

import com.tttm.Whear.App.entity.NewsImages;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.repository.NewsImageRepository;
import com.tttm.Whear.App.service.NewsImageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsImageServiceImpl implements NewsImageService {
    private final NewsImageRepository newsImageRepository;
    private final Logger logger = LoggerFactory.getLogger(NewsImageServiceImpl.class);

    @Override
    public void createNewImages(Integer newsID, String image) throws CustomException {
        newsImageRepository.save(
                NewsImages
                        .builder()
                        .imageUrl(image)
                        .newsID(newsID)
                        .build()
        );
    }

    @Override
    public void deleteNewsImages(Integer newsID) throws CustomException {
        newsImageRepository.deleteNewsImagesByNewsID(newsID);
    }

    @Override
    public List<NewsImages> getNewsImagesByNewsID(Integer newsID) throws CustomException
    {
        return newsImageRepository.getNewsImagesByNewsID(newsID);
    }
}
