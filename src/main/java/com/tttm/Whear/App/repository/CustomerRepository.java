package com.tttm.Whear.App.repository;

import com.tttm.Whear.App.entity.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    @Modifying
    @Transactional
    @Query(value = "update customer set number_of_update_style = ?1, last_modified_date = current_timestamp where customerid = ?2", nativeQuery = true)
    void updateNumberOfTimesChangeStyleByCustomerID(Integer numberOfUpdateStyle, String customerID);
}
