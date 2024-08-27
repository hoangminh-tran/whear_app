package com.tttm.Whear.App.repository;

import com.tttm.Whear.App.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {

  @Query(value = "select * from payment where paymentid = ?1", nativeQuery = true)
  Payment getByPaymentID(Integer paymentID);

  @Query(value = "select create_date from payment where paymentid = ?1", nativeQuery = true)
  String getDateTime(Integer paymentID);

  @Query(value = "select * from payment where customerid = ?1", nativeQuery = true)
  List<Payment> getAllPayment(String userId);
}
