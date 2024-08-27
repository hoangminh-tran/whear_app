package com.tttm.Whear.App.repository;

import com.tttm.Whear.App.entity.BodyShape;
import com.tttm.Whear.App.entity.Style;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BodyShapeRepository extends JpaRepository<BodyShape, Integer> {
    @Query
    (
            value = "select * from body_shape where body_shape_name = ?1", nativeQuery = true
    )
    BodyShape getBodyShapeByBodyShapeName(String bodyShapeName);
}
