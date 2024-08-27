package com.tttm.Whear.App.repository;

import com.tttm.Whear.App.entity.ClothesSeason;
import com.tttm.Whear.App.entity.ClothesSeasonKey;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClothesSeasonRepository extends JpaRepository<ClothesSeason, ClothesSeasonKey> {

  @Modifying
  @Transactional
  @Query(value = "insert into clothes_season (clothesid, season, create_date, last_modified_date) values (?1, ?2, current_timestamp, null)", nativeQuery = true)
  void insertClothesSeason(Integer clothesid, String season);

  @Modifying
  @Transactional
  @Query(value = "delete from clothes_season where clothes_season.clothesid = ?1 and clothes_season.season = ?2", nativeQuery = true)
  void deleteClothesSeason(Integer clothesid, String season);

  @Transactional
  @Query(value = "select * from clothes_season where clothes_season.clothesid = ?1 and clothes_season.season = ?2", nativeQuery = true)
  ClothesSeason findByName(Integer clothesid, String season);

  @Transactional
  @Query(value = "select * from clothes_season where clothes_season.clothesid = ?1", nativeQuery = true)
  List<ClothesSeason> getAllSeasonOfClothes(Integer clothesid);

  @Modifying
  @Transactional
  @Query(value = "delete from clothes_season where clothes_season.clothesid = ?1", nativeQuery = true)
  void deleteByClothesID(Integer clothesid);
}
