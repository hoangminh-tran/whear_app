package com.tttm.Whear.App.service.impl;

import com.tttm.Whear.App.constant.ConstantMessage;
import com.tttm.Whear.App.entity.News;
import com.tttm.Whear.App.entity.NewsImages;
import com.tttm.Whear.App.enums.StatusGeneral;
import com.tttm.Whear.App.enums.TypeOfNews;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.repository.NewsImageRepository;
import com.tttm.Whear.App.repository.NewsRepository;
import com.tttm.Whear.App.service.BrandService;
import com.tttm.Whear.App.service.NewsImageService;
import com.tttm.Whear.App.service.NewsService;
import com.tttm.Whear.App.utils.request.NewsRequest;
import com.tttm.Whear.App.utils.response.NewsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.DateFormatter;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final NewsImageService newsImageService;
    private final BrandService brandService;

    @Override
    public NewsResponse createNews(NewsRequest newsRequest) throws CustomException {
        if (newsRequest == null) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }
        if (newsRequest.getBrandID() == null) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }
        if (newsRequest.getTypeOfNews() == null) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }
        if (newsRequest.getTitle() == null) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }
        if (newsRequest.getContent() == null) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }
        if (newsRequest.getImage() == null) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }

        Integer brandID = newsRequest.getBrandID();
        if (brandService.getBrandByID(brandID) == null) {
            throw new CustomException(ConstantMessage.RESOURCE_NOT_FOUND.getMessage());
        }
        String typeOfNews = newsRequest.getTypeOfNews().trim();
        if (typeOfNews.isEmpty() || typeOfNews.isBlank()) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }
        String title = newsRequest.getTitle().trim();
        if (title.isEmpty() || title.isBlank()) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }
        String content = newsRequest.getContent().trim();
        if (content.isEmpty() || content.isBlank()) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }

        List<String> image = newsRequest.getImage();
        if (image.size() == 0) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }

        newsRepository.saveNews(brandID, typeOfNews, title, content, Boolean.parseBoolean(newsRequest.getStatus()));

        News news = newsRepository.getNewsByNewsDetails(brandID, typeOfNews, title, content, Boolean.parseBoolean(newsRequest.getStatus()));
        if (news != null) {
            for (String img : image) {
                newsImageService.createNewImages(news.getNewsID(), img);
            }
        }
        return convertToNewsResponse(news.getNewsID());
    }

    @Override
    public NewsResponse updateNews(NewsRequest newsRequest) throws CustomException {
        if (newsRequest == null) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }
        if (newsRequest.getNewsID() == null) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.name());
        }
        Integer newsID = newsRequest.getNewsID();

        News news = newsRepository.getNewsByNewsIDIs(newsID);
        if (news == null) {
            throw new CustomException(ConstantMessage.RESOURCE_NOT_FOUND.getMessage());
        }
        if (newsRequest.getBrandID() == null) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }
        if (newsRequest.getTypeOfNews() == null) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }
        if (newsRequest.getTitle() == null) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }
        if (newsRequest.getContent() == null) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }

        Integer brandID = newsRequest.getBrandID();
        String title = newsRequest.getTitle().trim();
        String content = newsRequest.getContent().trim();
        String typeOfNews = newsRequest.getTypeOfNews().trim();

        newsRepository.updateNews(brandID, typeOfNews, title, content, Boolean.parseBoolean(newsRequest.getStatus()), newsID);

        return convertToNewsResponse(newsID);
    }

    @Override
    public void deleteNews(NewsRequest newsRequest) throws CustomException {
        if (newsRequest == null) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }
        if (newsRequest.getNewsID() == null) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.name());
        }
        Integer newsID = newsRequest.getNewsID();

        News news = newsRepository.getNewsByNewsIDIs(newsID);
        if (news == null) {
            throw new CustomException(ConstantMessage.RESOURCE_NOT_FOUND.getMessage());
        }
        newsImageService.deleteNewsImages(newsID);
        newsRepository.deleteByNewsID(newsRequest.getNewsID());
    }

    @Override
    public NewsResponse getNewsByID(Integer newsID) throws CustomException {
        if (newsID == null) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }
        News news = newsRepository.getNewsByNewsIDIs(newsID);
        if (news == null) {
            throw new CustomException(ConstantMessage.RESOURCE_NOT_FOUND.getMessage());
        }
        return convertToNewsResponse(news.getNewsID());
    }

    @Override
    public List<NewsResponse> getAllNews() throws CustomException {
        List<News> newsList = newsRepository.findAll();
        List<NewsResponse> responseList = new ArrayList<>();
        for (News news : newsList) {
            responseList.add(convertToNewsResponse(news.getNewsID()));
        }
        return responseList;
    }

    @Override
    public List<NewsResponse> getNewsByBrandID(Integer brandID) throws CustomException {
        if (brandID == null) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }
        if (brandService.getBrandByID(brandID) == null) {
            throw new CustomException(ConstantMessage.RESOURCE_NOT_FOUND.getMessage());
        }

        List<News> newsList = newsRepository.getNewsByBrandID(brandID);
        List<NewsResponse> responseList = new ArrayList<>();
        for (News news : newsList) {
            responseList.add(convertToNewsResponse(news.getNewsID()));
        }
        return responseList;
    }

    @Override
    public List<NewsResponse> getNewsByTypeOfNews(NewsRequest newsRequest) throws CustomException {
        if (newsRequest == null) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }
        if (newsRequest.getTypeOfNews() == null) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }

        List<News> newsList = newsRepository.getNewsByTypeOfNews(newsRequest.getTypeOfNews());
        List<NewsResponse> responseList = new ArrayList<>();
        for (News news : newsList) {
            responseList.add(convertToNewsResponse(news.getNewsID()));
        }
        return responseList;
    }

    private NewsResponse convertToNewsResponse(Integer newsID) throws CustomException {
        News news = newsRepository.getNewsByNewsIDIs(newsID);
        if (news == null) {
            return null;
        }
        List<NewsImages> img = newsImageService.getNewsImagesByNewsID(news.getNewsID());
        List<String> imgList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (NewsImages i : img) {
            imgList.add(i.getImageUrl());
        }
        LocalDateTime dateTime = (news.getLastModifiedDate() == null) ? news.getCreateDate() : news.getLastModifiedDate();
        return NewsResponse
                .builder()
                .brand(
                        brandService.getBrandByID(Integer.parseInt(news.getBrandID()))
                )
                .newsID(news.getNewsID())
                .typeOfNews(news.getTypeOfNews().name())
                .title(news.getTitle())
                .content(news.getContent())
                .status(news.getStatus().name())
                .image(imgList)
                .date(dateTime.format(formatter))
                .build();
    }
}
