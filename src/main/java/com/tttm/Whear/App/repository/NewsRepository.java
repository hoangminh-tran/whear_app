package com.tttm.Whear.App.repository;

import com.tttm.Whear.App.entity.News;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Integer> {
    @Modifying
    @Transactional
    @Query(value = "insert into news (brandid, type_of_news, title, content, status, create_date, last_modified_date) values (?1, ?2, ?3, ?4, ?5, current_timestamp, null)", nativeQuery = true)
    void saveNews(Integer brandID, String typeOfNews, String title, String content, Boolean status);

    @Modifying
    @Transactional
    @Query(value = "update news set brandid = ?1, type_of_news = ?2, title = ?3, content = ?4, status = ?5, last_modified_date = current_timestamp where newsid = ?6 ", nativeQuery = true)
    void updateNews(Integer brandID, String typeOfNews, String title, String content, Boolean status, Integer newID);

    @Modifying
    @Transactional
    @Query(value = "delete from news where newsid = ?1", nativeQuery = true)
    void deleteByNewsID(Integer postID);

    News getNewsByNewsIDIs(Integer newID);
    @Query(value = "select * from news where brandid = ?1 and type_of_news = ?2 and title = ?3 and content = ?4 and status = ?5", nativeQuery = true)
    News getNewsByNewsDetails(Integer brandID, String typeOfNews, String title, String content, Boolean status);

    @Query(value = "select * from news where brandid = ?1 and type_of_news = ?2 and title = ?3 and content = ?4 and status = ?5 and newsid = ?6", nativeQuery = true)
    News getNewsByNewsEntity(Integer brandID, String typeOfNews, String title, String content, Boolean status, Integer newID);

    @Query(value = "select * from news where brandid = ?1 order by create_date desc", nativeQuery = true)
    List<News> getNewsByBrandID(Integer brandID);

    @Query(value = "select * from news where type_of_news = ?1 order by create_date desc", nativeQuery = true)
    List<News> getNewsByTypeOfNews(String typeOfNews);
}
