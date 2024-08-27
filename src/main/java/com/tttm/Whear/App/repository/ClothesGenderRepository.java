package com.tttm.Whear.App.repository;

import com.tttm.Whear.App.entity.ClothesColor;
import com.tttm.Whear.App.entity.ClothesColorKey;
import com.tttm.Whear.App.entity.ClothesGender;
import com.tttm.Whear.App.entity.ClothesGenderKey;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClothesGenderRepository extends JpaRepository<ClothesGender, ClothesGenderKey> {
    @Modifying
    @Transactional
    @Query(value = "insert into clothes_gender (clothesid, gender_type, create_date, last_modified_date) values (?1, ?2, current_timestamp, null)", nativeQuery = true)
    void insertClothesGender(Integer clothesid, String gender_type);

    @Modifying
    @Transactional
    @Query(value = "delete from clothes_gender where clothes_gender.clothesid = ?1 and clothes_gender.gender_type = ?2", nativeQuery = true)
    void deleteClothesGender(Integer clothesid, String gender_type);

    @Transactional
    @Query(value = "select * from clothes_gender where clothes_gender.clothesid = ?1 and clothes_gender.gender_type = ?2", nativeQuery = true)
    ClothesGender findByName(Integer clothesid, String gender_type);

    @Transactional
    @Query(value = "select * from clothes_gender where clothes_gender.clothesid = ?1", nativeQuery = true)
    List<ClothesGender> getAllGenderOfClothes(Integer clothesid);

    @Modifying
    @Transactional
    @Query(value = "delete from clothes_gender where clothes_gender.clothesid = ?1", nativeQuery = true)
    void deleteByClothesID(Integer clothesid);
}
