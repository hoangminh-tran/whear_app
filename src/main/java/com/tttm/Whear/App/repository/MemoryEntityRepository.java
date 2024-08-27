package com.tttm.Whear.App.repository;

import com.tttm.Whear.App.entity.MemoryEntity;
import jakarta.transaction.Transactional;
import org.checkerframework.checker.units.qual.A;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoryEntityRepository extends JpaRepository<MemoryEntity, Integer> {
    @Query(value = "select * from memory_entity where " +
            "style_name = ?1 and " +
            "body_shape_name = ?2 and " +
            "(top_insideid = ?3 OR top_insideid IS NULL) and " +
            "(top_inside_color = ?4 OR top_inside_color IS NULL) and " +
            "(top_outsideid = ?5 OR top_outsideid IS NULL) and " +
            "(top_outside_color = ?6 OR top_outside_color IS NULL) and " +
            "(bottom_kindid = ?7 OR bottom_kindid IS NULL) and " +
            "(bottom_color = ?8 OR bottom_color IS NULL) and " +
            "(shoes_typeid = ?9 OR shoes_typeid IS NULL) and " +
            "(shoes_type_color = ?10 OR shoes_type_color IS NULL) and " +
            "(accessory_kindid = ?11 OR accessory_kindid IS NULL)", nativeQuery = true)
    MemoryEntity getMemoryByMemoryRequest(String styleName, String bodyShapeName, String topInsideID, String topInsideColor, String topOutsideID, String topOutsideColor,
                                          String bottomKindID, String bottomColor, String shoesTypeID, String shoesTypeColor, String accessoryKindID);

    @Query(
            value = "SELECT COUNT(*) FROM memory_entity WHERE style_name = ?1 AND body_shape_name = ?2", nativeQuery = true
    )
    Integer countMemoryByStyleAndBodyShape(String styleName, String bodyShapeName);

    @Modifying
    @Transactional
    @Query(value = "update memory_entity set dislike_clothes_by_user = ?1,last_modified_date = current_timestamp where memoryid = ?2", nativeQuery = true)
    void updateMemoryEntityForDislikeClothes(String dislikeUser, Integer memoryID);

    @Modifying
    @Transactional
    @Query(value = "update memory_entity set suggest_clothes_to_user = ?1,last_modified_date = current_timestamp where memoryid = ?2", nativeQuery = true)
    void updateMemoryEntityForSuggestClothes(String suggestUser, Integer memoryID);

    @Query(value = "select * from memory_entity where " +
            "style_name = ?1 and " +
            "body_shape_name = ?2 and " +
            "(top_insideid = ?3 OR top_insideid IS NULL) and " +
            "(top_outsideid = ?4 OR top_outsideid IS NULL) and " +
            "(bottom_kindid = ?5 OR bottom_kindid IS NULL) and " +
            "(shoes_typeid = ?6 OR shoes_typeid IS NULL) and " +
            "(accessory_kindid = ?7 OR accessory_kindid IS NULL)", nativeQuery = true)
    MemoryEntity getMemoryForRejectClothesRequest(String styleName, String bodyShapeName, String topInsideID, String topOutsideID,
                                          String bottomKindID, String shoesTypeID, String accessoryKindID);

    @Query(
            value = "SELECT COUNT(*) FROM memory_entity WHERE style_name = ?1 AND body_shape_name = ?2 AND dislike_clothes_by_user LIKE %?3%", nativeQuery = true
    )
    Integer countDislikeOutfitByStyleBodyShapeAndUserID(String styleName, String bodyShapeName, String dislike_clothes_by_user);
    @Query(
            value = "SELECT COUNT(*) FROM memory_entity WHERE style_name = ?1 AND body_shape_name = ?2 AND suggest_clothes_to_user LIKE %?3%", nativeQuery = true
    )
    Integer countSuggestOutfitByStyleBodyShapeAndUserID(String styleName, String bodyShapeName, String suggest_clothes_to_user);

    @Query(
            value = "SELECT COUNT(*) FROM memory_entity WHERE style_name = ?1 AND body_shape_name = ?2 AND suggest_clothes_to_user LIKE %?3% AND dislike_clothes_by_user LIKE %?3%", nativeQuery = true
    )
    Integer countDislikeAndSuggestOutfitByStyleBodyShapeAndUserID(String styleName, String bodyShapeName, String userID);

    @Query(
            value = "Select * from memory_entity where style_name = ?1 AND " +
                    "body_shape_name = ?2 AND " +
                    "(suggest_clothes_to_user LIKE %?3% OR " +
                    "dislike_clothes_by_user LIKE %?3%)",
            nativeQuery = true
    )
    List<MemoryEntity> getAllMemoryEntityByStyleBodyShapeAndUser(String styleName, String bodyShapeName, String userID);

    @Query(
            value = "Select * from memory_entity where style_name = ?1 AND " +
                    "body_shape_name = ?2 AND " +
                    "(suggest_clothes_to_user LIKE %?3% OR " +
                    "dislike_clothes_by_user LIKE %?3%) AND " +
                    "user_accepted_old_outfit LIKE %?3%",
            nativeQuery = true
    )
    List<MemoryEntity> getAllMemoryEntityByStyleBodyShapeUserAcceptOldOutfit(String styleName, String bodyShapeName, String userID);


    @Modifying
    @Transactional
    @Query(value = "update memory_entity set user_accepted_old_outfit = ?1, last_modified_date = current_timestamp where memoryid = ?2", nativeQuery = true)
    void updateMemoryEntitiesWhenAcceptOldOutfit(String userID, Integer memoryEntityID);
}
