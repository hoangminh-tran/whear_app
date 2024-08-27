package com.tttm.Whear.App.utils.request;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentItem implements Serializable {

  private String name;
  private Integer quantity;
  private Integer price;
}
