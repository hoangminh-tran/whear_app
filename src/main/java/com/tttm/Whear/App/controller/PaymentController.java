package com.tttm.Whear.App.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tttm.Whear.App.constant.APIConstant.PaymentAPI;
import com.tttm.Whear.App.service.PaymentService;
import com.tttm.Whear.App.utils.request.PaymentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping(PaymentAPI.PAYMENT)
public class PaymentController {
  private final PaymentService paymentService;

  @PostMapping(PaymentAPI.CREATE_PAYMENT)
  public ObjectNode createPayment(@RequestBody PaymentRequest paymentRequest) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      ObjectNode respon = objectMapper.createObjectNode();
      respon.put("success", 200);
      respon.put("message", "Create payment Successfully");
      respon.set("data",
          objectMapper.valueToTree(paymentService.createPayment(paymentRequest)));
      return respon;
    } catch (Exception ex) {
      ObjectNode respon = objectMapper.createObjectNode();
      respon.put("error", -1);
      respon.put("message", ex.getMessage());
      respon.set("data", null);
      return respon;
    }
  }

  @GetMapping(PaymentAPI.GET_PAYMENT_BY_ID)
  public ObjectNode getPaymentByID(@RequestParam("orderCode") Integer orderCode) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      ObjectNode respon = objectMapper.createObjectNode();
      respon.put("success", 200);
      respon.put("message", "Create payment Successfully");
      respon.set("data",
              objectMapper.valueToTree(
                      paymentService.getPaymentInfor(
                              orderCode.toString()
                      )
              )
      );
      return respon;
    } catch (Exception ex) {
      ObjectNode respon = objectMapper.createObjectNode();
      respon.put("error", -1);
      respon.put("message", ex.getMessage());
      respon.set("data", null);
      return respon;
    }
  }

  @GetMapping(PaymentAPI.GET_ALL_PAYMENT)
  public ObjectNode getAllPayment(@RequestParam("userId") String userId) {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      ObjectNode respon = objectMapper.createObjectNode();
      respon.put("success", 200);
      respon.put("message", "get payment Successfully");
      respon.set("data",
              objectMapper.valueToTree(
                      paymentService.getAllPayment(userId)
              )
      );
      return respon;
    } catch (Exception ex) {
      ObjectNode respon = objectMapper.createObjectNode();
      respon.put("error", -1);
      respon.put("message", ex.getMessage());
      respon.set("data", null);
      return respon;
    }
  }

  @PutMapping(path = "/confirm-update")
  public ObjectNode confirmUpdate(@RequestParam("orderCode") Integer orderCode, @RequestParam("item") String item) {
    ObjectMapper objectMapper = new ObjectMapper();
    ObjectNode respon = objectMapper.createObjectNode();
    try {
      paymentService.confirmUpdate(orderCode, item);
      respon.put("success", 200);
      respon.put("message", "ok");
      respon.set("data", null);
      return respon;
    } catch (Exception e) {
      e.printStackTrace();
      respon.put("error", -1);
      respon.put("message", e.getMessage());
      respon.set("data", null);
      return respon;
    }
  }

//  @GetMapping(PaymentAPI.GET_ALL_PAYMENT)
//  public ObjectNode getAllPayment() {
//    ObjectMapper objectMapper = new ObjectMapper();
//    try {
//      ObjectNode respon = objectMapper.createObjectNode();
//      respon.put("success", 200);
//      respon.put("message", "Get All Payment Successfully");
//      respon.set("data",
//          objectMapper.valueToTree(
//              paymentService.getAllPayment()
//          )
//      );
//      return respon;
//    } catch (Exception ex) {
//      ObjectNode respon = objectMapper.createObjectNode();
//      respon.put("error", -1);
//      respon.put("message", ex.getMessage());
//      respon.set("data", null);
//      return respon;
//    }
//  }
}
