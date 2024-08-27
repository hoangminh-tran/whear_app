package com.tttm.Whear.App.repository;

import com.tttm.Whear.App.entity.CollectionClothes;
import com.tttm.Whear.App.entity.CollectionClothesKey;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionClothesRepository extends
    JpaRepository<CollectionClothes, CollectionClothesKey> {

  @Modifying
  @Transactional
  @Query(value = "insert into collection_clothes (clothesid, collectionid, create_date, last_modified_date) values (?1, ?2, current_timestamp, null)", nativeQuery = true)
  void insertClothesToCollection(Integer clothesID, Integer collectionID);

  @Modifying
  @Transactional
  @Query(value = "delete from collection_clothes where clothesid = ?1 and collectionid = ?2", nativeQuery = true)
  void deleteClothesToCollection(Integer clothesID, Integer collectionID);

  @Modifying
  @Transactional
  @Query(value = "delete from collection_clothes where collectionid = ?1", nativeQuery = true)
  void deleteByCollectionID(Integer collectionID);

  @Query(value = "select * from collection_clothes where collectionid = ?1", nativeQuery = true)
  List<CollectionClothes> getAllByCollectionID(Integer collectionID);

  @Query(value = "select * from collection_clothes where clothesid = ?1 and collectionid = ?2", nativeQuery = true)
  CollectionClothes checkContain(Integer clothesID, Integer collectionID);
}
