package com.tttm.Whear.App.repository;

import com.tttm.Whear.App.entity.ClothesStyle;
import com.tttm.Whear.App.entity.ClothesStyleKey;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClothesStyleRepository extends JpaRepository<ClothesStyle, ClothesStyleKey> {

  @Modifying
  @Transactional
  @Query(value = "insert into clothes_style (clothesid, styleid, create_date, last_modified_date) values (?1, ?2, current_timestamp, null)", nativeQuery = true)
  void insertClothesStyle(Integer clothesid, Integer styleID);

  @Transactional
  @Query(value = "select * from clothes_style where clothes_style.clothesid = ?1 and clothes_style.styleid = ?2", nativeQuery = true)
  ClothesStyle findByName(Integer clothesid, Integer size);

  @Transactional
  @Query(value = "select * from clothes_style where clothes_style.clothesid = ?1", nativeQuery = true)
  List<ClothesStyle> getAllStyleOfClothes(Integer clothesid);

  @Modifying
  @Transactional
  @Query(value = "delete from clothes_style where clothes_style.clothesid = ?1", nativeQuery = true)
  void deleteByClothesID(Integer clothesid);
}
