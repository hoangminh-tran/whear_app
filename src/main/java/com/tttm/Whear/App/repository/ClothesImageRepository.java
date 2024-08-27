package com.tttm.Whear.App.repository;

import com.tttm.Whear.App.entity.ClothesImage;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClothesImageRepository extends JpaRepository<ClothesImage, Integer> {
  public List<ClothesImage> getAllByClothesID(Integer clothesID);
  @Modifying
  @Transactional
  @Query(value = "delete from clothes_image where clothesid = ?1", nativeQuery = true)
  void deleteByClothesID(Integer clothesid);
}
