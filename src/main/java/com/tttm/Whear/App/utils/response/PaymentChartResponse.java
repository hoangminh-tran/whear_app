package com.tttm.Whear.App.utils.response;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentChartResponse implements Serializable {
  private String month;
  private Integer amount;
}
