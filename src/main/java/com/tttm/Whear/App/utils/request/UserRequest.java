package com.tttm.Whear.App.utils.request;

import com.tttm.Whear.App.enums.ERole;
import com.tttm.Whear.App.enums.Language;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
  private String userID;
  private String username;

  private String password;

  private Date dateOfBirth;

  private String phone;

  private String email;

  private Boolean gender;

  private String imgUrl;

  private ERole role;

  private Language language;
}
