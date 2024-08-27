package com.tttm.Whear.App.repository;

import com.tttm.Whear.App.entity.ClothesColor;
import com.tttm.Whear.App.entity.ClothesColorKey;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClothesColorRepository extends JpaRepository<ClothesColor, ClothesColorKey> {

  @Modifying
  @Transactional
  @Query(value = "insert into clothes_color (clothesid, color, create_date, last_modified_date) values (?1, ?2, current_timestamp, null)", nativeQuery = true)
  void insertClothesColor(Integer clothesid, String color);

  @Modifying
  @Transactional
  @Query(value = "delete from clothes_color where clothes_color.clothesid = ?1 and clothes_color.color = ?2", nativeQuery = true)
  void deleteClothesColor(Integer clothesid, String color);

  @Transactional
  @Query(value = "select * from clothes_color where clothes_color.clothesid = ?1 and clothes_color.color = ?2", nativeQuery = true)
  ClothesColor findByName(Integer clothesid, String color);

  @Transactional
  @Query(value = "select * from clothes_color where clothes_color.clothesid = ?1", nativeQuery = true)
  List<ClothesColor> getAllColorOfClothes(Integer clothesid);

  @Modifying
  @Transactional
  @Query(value = "delete from clothes_color where clothes_color.clothesid = ?1", nativeQuery = true)
  void deleteByClothesID(Integer clothesid);
}
