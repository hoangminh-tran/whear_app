package com.tttm.Whear.App.utils.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponse {

  private String code;
  private String desc;
  private PaymentData data;
  private String signature;
}
