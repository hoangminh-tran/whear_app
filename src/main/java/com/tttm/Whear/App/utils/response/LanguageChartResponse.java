package com.tttm.Whear.App.utils.response;

import java.io.Serializable;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LanguageChartResponse implements Serializable {
  private String language;
  private Integer quantity;
}
