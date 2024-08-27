package com.tttm.Whear.App.repository;

import com.tttm.Whear.App.entity.Brand;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {
    @Modifying
    @Transactional
    @Query(value = "insert into brand (brandid, brand_name, description, link, address, create_date, last_modified_date) values (?1, ?2, ?3, ?4, ?5, current_timestamp, null)", nativeQuery = true)
    void createNewBrand(String brandid, String brand_name, String description, String link, String address);

    @Query(value = "select b.* from brand b join customer c on b.brandID = c.customerID", nativeQuery = true)
    public List<Brand> getAllBrand();

    Brand getBrandByBrandID(String brandID);
}
