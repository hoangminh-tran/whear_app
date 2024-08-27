package com.tttm.Whear.App.utils.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentData {

  private String bin;
  private String accountNumber;
  private String accountName;
  private String currency;
  private String paymentLinkId;
  private Integer amount;
  private String description;
  private Integer orderCode;
  private String status;
  private String checkoutUrl;
  private String qrCode;
}
