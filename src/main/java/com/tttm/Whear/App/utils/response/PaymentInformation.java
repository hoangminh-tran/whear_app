package com.tttm.Whear.App.utils.response;

import com.tttm.Whear.App.entity.Payment;
import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentInformation implements Serializable {

  private String code;
  private String desc;
  private PaymentInformationData data;
  private String signature;
  private String checkoutUrl;
  private String qrCode;
}
