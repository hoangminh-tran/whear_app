package com.tttm.Whear.App.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tttm.Whear.App.entity.Payment;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.utils.request.PaymentRequest;
import com.tttm.Whear.App.utils.response.PaymentInformation;
import com.tttm.Whear.App.utils.response.PaymentResponse;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface PaymentService {

  PaymentResponse createPayment(PaymentRequest paymentRequest)
      throws CustomException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException;

  PaymentInformation getPaymentInfor(String paymentID)
      throws CustomException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException;

  List<PaymentInformation> getAllPayment(String userId) throws Exception;

  List<Payment> getAllPayment() throws Exception;

  String getDateTime(Integer paymentID);

  void confirmUpdate(Integer orderCode, String item) throws CustomException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException;
}
