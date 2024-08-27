package com.tttm.Whear.App.repository;

import com.tttm.Whear.App.entity.RuleMatchingClothes;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleMatchingClothesRepository extends JpaRepository<RuleMatchingClothes, Integer> {
    @Modifying
    @Transactional
    @Query(value = "insert into rule_matching_clothes (styleid, body_shapeid, top_inside, top_inside_color, top_outside, top_outside_color, " +
            "top_material, bottom_kind, bottom_color, shoes_type, shoes_type_color, bottom_material, accessory_kind, accessory_material, " +
            "create_date, last_modified_date) values (?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, ?10, ?11, ?12, ?13, ?14, current_timestamp, null)", nativeQuery = true)
    void createNewRuleMatchingClothes(Integer style_type, Integer body_shape_type, String top_inside, String top_inside_color, String top_outside, String top_outside_color,
                                      String top_material, String bottom_kind, String bottom_color, String shoes_type, String shoes_type_color, String bottom_material,
                                      String accessory_kind, String accessory_material);

    @Query(value = "select * from rule_matching_clothes where styleid = ?1 and body_shapeid = ?2", nativeQuery = true)
    RuleMatchingClothes getRuleMatchingClothesByStyleAndBodyShape(Integer styleID, Integer bodyShapeID);
}
