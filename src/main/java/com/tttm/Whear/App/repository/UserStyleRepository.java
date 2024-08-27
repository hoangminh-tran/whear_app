package com.tttm.Whear.App.repository;

import com.tttm.Whear.App.entity.Style;
import com.tttm.Whear.App.entity.UserStyle;
import com.tttm.Whear.App.entity.UserStyleKey;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserStyleRepository extends JpaRepository<UserStyle, UserStyleKey> {
    @Modifying
    @Transactional
    @Query(value = "insert into user_style (userid, styleid, style_position, create_date, last_modified_date) values (?1, ?2, ?3, current_timestamp, null)", nativeQuery = true)
    void createUserStyle(String userid, Integer styelid, Integer index);

    @Query(value = "select * from user_style where styleid = ?1 and userid = ?2", nativeQuery = true)
    UserStyle findUserStyleByStyleIDAndUserID(Integer styleID, String userID);

    @Modifying
    @Transactional
    @Query(value = "update user_style set styleid = ?1, last_modified_date = current_timestamp where userid = ?2 and styleid = ?3", nativeQuery = true)
    void updateUserStyle(Integer newStyleID, String userID, Integer oldStyleID);

    @Query(value = "select * from user_style where userid = ?1", nativeQuery = true)
    List<UserStyle> getListUserStyleByUserID(String userID);
}
