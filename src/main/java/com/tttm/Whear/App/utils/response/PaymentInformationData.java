package com.tttm.Whear.App.utils.response;

import java.io.Serializable;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentInformationData implements Serializable {

  private String id;
  private Integer orderCode;
  private Integer amount;
  private Integer amountPaid;
  private Integer amountRemaining;
  private String status;
  private String createdAt;
  private List<Transactions> transactions;
  private String canceledAt;
  private String cancellationReason;
}
