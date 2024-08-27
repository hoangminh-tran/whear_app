package com.tttm.Whear.App.repository;

import com.tttm.Whear.App.entity.Clothes;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClothesRepository extends JpaRepository<Clothes, Integer> {

  @Modifying
  @Transactional
  @Query(value = "insert into clothes (clothesid, description, link, materials, name_of_product, rating, shape, type_of_clothes, create_date, last_modified_date) values (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, current_timestamp, null)", nativeQuery = true)
  void insertClothes(Integer clothesid, String desctiption, String link, String materials,
      String name, Integer rating, String shape, String type);

  @Query(value = "select clothesid, name_of_product, type_of_clothes, shape, description, link, rating, materials, create_date, last_modified_date from clothes where clothesid = ?1", nativeQuery = true)
  public Clothes getClothesByClothesID(Integer clothesID);

  @Query(value = "select c.* from clothes c JOIN posts p on c.clothesid = p.postid where p.userid = ?1", nativeQuery = true)
  List<Clothes> getAllClothesByBrandID(String brandID);

  @Query(value = "SELECT c.* " +
          "FROM clothes c " +
          "JOIN clothes_color co ON c.clothesid = co.clothesid " +
          "WHERE c.type_of_clothes = ?1 " +
          "AND co.color LIKE %?2% " +
          "AND (c.materials LIKE %?3% OR c.materials != ?3)", nativeQuery = true)
  List<Clothes> getClothesBaseOnTypeOfClothesAndColorOrMaterials(String typeOfClothes, String color, String materials);

  @Query(value = "SELECT c.* " +
          "FROM clothes c " +
          "WHERE c.type_of_clothes = ?1 " +
          "AND (c.materials LIKE %?2% OR c.materials != ?2)", nativeQuery = true)
  List<Clothes> getClothesBaseOnTypeOfClothesAndMaterial(String typeOfClothes, String materials);
}
