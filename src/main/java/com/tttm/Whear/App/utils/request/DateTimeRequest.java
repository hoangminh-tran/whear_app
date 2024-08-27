package com.tttm.Whear.App.utils.request;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DateTimeRequest {

  private LocalDateTime startDate;
  private LocalDateTime endDate;
}
