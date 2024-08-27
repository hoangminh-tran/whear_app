package com.tttm.Whear.App.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tttm.Whear.App.constant.APIConstant.ChartAPI;
import com.tttm.Whear.App.entity.Payment;
import com.tttm.Whear.App.entity.User;
import com.tttm.Whear.App.enums.Language;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.service.PaymentService;
import com.tttm.Whear.App.service.UserService;
import com.tttm.Whear.App.utils.response.LanguageChartResponse;
import com.tttm.Whear.App.utils.response.PaymentChartResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(ChartAPI.CHART)
public class ChartController {

  private final UserService userService;
  private final PaymentService paymentService;

  @GetMapping(ChartAPI.LANGUAGE_CHART)
  public ObjectNode languageChart() throws CustomException {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      List<User> userList = userService.getAllUserEntity();
      List<LanguageChartResponse> responseList = new ArrayList<>();
      LanguageChartResponse vi = LanguageChartResponse
          .builder()
          .language(Language.VIETNAM.name())
          .quantity(0)
          .build();
      LanguageChartResponse en = LanguageChartResponse
          .builder()
          .language(Language.ENGLISH.name())
          .quantity(0)
          .build();
      for (User user : userList) {
        if (user.getLanguage().equals(Language.VIETNAM)) {
          vi.setQuantity(vi.getQuantity() + 1);
        } else {
          if (user.getLanguage().equals(Language.ENGLISH)) {
            en.setQuantity(en.getQuantity() + 1);
          }
        }
      }
      responseList.add(vi);
      responseList.add(en);
      ObjectNode respon = objectMapper.createObjectNode();
      respon.put("success", 200);
      respon.put("message", "Language chart");
      respon.set("data", objectMapper.valueToTree(responseList));
      return respon;
    } catch (Exception ex) {
      ObjectNode respon = objectMapper.createObjectNode();
      respon.put("error", -1);
      respon.put("message", ex.getMessage());
      respon.set("data", null);
      return respon;
    }
  }

  @GetMapping(ChartAPI.PAYMENT_CHART)
  public ObjectNode paymentChart() throws CustomException {
    ObjectMapper objectMapper = new ObjectMapper();
    try {
      List<Payment> paymentList = paymentService.getAllPayment();
      List<PaymentChartResponse> responseList = new ArrayList<>();

      PaymentChartResponse m1 = PaymentChartResponse
          .builder()
          .month("JAN")
          .amount(0)
          .build();
      PaymentChartResponse m2 = PaymentChartResponse
          .builder()
          .month("FEB")
          .amount(0)
          .build();
      PaymentChartResponse m3 = PaymentChartResponse
          .builder()
          .month("MAR")
          .amount(0)
          .build();
      PaymentChartResponse m4 = PaymentChartResponse
          .builder()
          .month("APR")
          .amount(0)
          .build();
      PaymentChartResponse m5 = PaymentChartResponse
          .builder()
          .month("MAY")
          .amount(0)
          .build();
      PaymentChartResponse m6 = PaymentChartResponse
          .builder()
          .month("JUN")
          .amount(0)
          .build();
      PaymentChartResponse m7 = PaymentChartResponse
          .builder()
          .month("JUL")
          .amount(0)
          .build();
      PaymentChartResponse m8 = PaymentChartResponse
          .builder()
          .month("AUG")
          .amount(0)
          .build();
      PaymentChartResponse m9 = PaymentChartResponse
          .builder()
          .month("SEP")
          .amount(0)
          .build();
      PaymentChartResponse m10 = PaymentChartResponse
          .builder()
          .month("OCT")
          .amount(0)
          .build();
      PaymentChartResponse m11 = PaymentChartResponse
          .builder()
          .month("NOV")
          .amount(0)
          .build();
      PaymentChartResponse m12 = PaymentChartResponse
          .builder()
          .month("DEC")
          .amount(0)
          .build();

      for (Payment p : paymentList) {
        String dateTime = paymentService.getDateTime(p.getPaymentID());
//        System.out.println(String.valueOf(LocalDateTime.now().getYear()));
        String[] parts = dateTime.split("-");
        if (Integer.parseInt(parts[0]) == LocalDateTime.now().getYear()) {
          System.out.println(parts[0]);
          System.out.println(parts[1]);
          switch (Integer.parseInt(parts[1])) {
            case 1:
              m1.setAmount(
                  m1.getAmount() + p.getValue()
              );
              break;
            case 2:
              m2.setAmount(
                  m2.getAmount() + p.getValue()
              );
              break;
            case 3:
              m3.setAmount(
                  m3.getAmount() + p.getValue()
              );
              break;
            case 4:
              m4.setAmount(
                  m4.getAmount() + p.getValue()
              );
              break;
            case 5:
              m5.setAmount(
                  m5.getAmount() + p.getValue()
              );
              break;
            case 6:
              m6.setAmount(
                  m6.getAmount() + p.getValue()
              );
              break;
            case 7:
              m7.setAmount(
                  m7.getAmount() + p.getValue()
              );
              break;
            case 8:
              m8.setAmount(
                  m8.getAmount() + p.getValue()
              );
              break;
            case 9:
              m9.setAmount(
                  m9.getAmount() + p.getValue()
              );
              break;
            case 10:
              m10.setAmount(
                  m10.getAmount() + p.getValue()
              );
              break;
            case 11:
              m11.setAmount(
                  m11.getAmount() + p.getValue()
              );
              break;
            case 12:
              m12.setAmount(
                  m12.getAmount() + p.getValue()
              );
              break;
          }
        }
      }
      responseList.add(m1);
      responseList.add(m2);
      responseList.add(m3);
      responseList.add(m4);
      responseList.add(m5);
      responseList.add(m6);
      responseList.add(m7);
      responseList.add(m8);
      responseList.add(m9);
      responseList.add(m10);
      responseList.add(m11);
      responseList.add(m12);
      ObjectNode respon = objectMapper.createObjectNode();
      respon.put("success", 200);
      respon.put("message", "Payment chart");
      respon.set("data", objectMapper.valueToTree(responseList));
      return respon;
    } catch (Exception ex) {
      ObjectNode respon = objectMapper.createObjectNode();
      respon.put("error", -1);
      respon.put("message", ex.getMessage());
      respon.set("data", null);
      return respon;
    }
  }
}
