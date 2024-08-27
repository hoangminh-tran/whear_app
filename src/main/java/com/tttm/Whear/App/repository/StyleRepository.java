package com.tttm.Whear.App.repository;

import com.tttm.Whear.App.entity.Style;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StyleRepository extends JpaRepository<Style, Integer> {
    @Query(
            value = "select * from style where style_name = ?1", nativeQuery = true
    )
    Style getStyleByStyleName(String styleName);

    @Query(
            value = "select s.* from style s join user_style us on s.styleid = us.styleid where us.userid = ?1 " +
                    "ORDER BY us.style_position ASC", nativeQuery = true
    )
    List<Style> getListStyleNameByUserID(String userID);
}
