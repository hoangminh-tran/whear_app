package com.tttm.Whear.App.repository;

import com.tttm.Whear.App.entity.NewsImages;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsImageRepository extends JpaRepository<NewsImages, Integer> {
    List<NewsImages> getNewsImagesByNewsID(Integer newsID);

    @Modifying
    @Transactional
    @Query(value = "delete from news_image where newsid = ?1", nativeQuery = true)
    void deleteNewsImagesByNewsID(Integer newsID);
}
