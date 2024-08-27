package com.tttm.Whear.App.utils.response;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Transactions implements Serializable {

  private Integer amount;
  private String description;
  private String accountNumber;
  private String reference;
  private String transactionDateTime;
  private String counterAccountBankId;
  private String counterAccountBankName;
  private String counterAccountName;
  private String counterAccountNumber;
  private String virtualAccountName;
  private String virtualAccountNumber;
}
