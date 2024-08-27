package com.tttm.Whear.App.utils.response;

import com.tttm.Whear.App.enums.ESubRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubroleResponse {

  private Integer subRoleID;
  private ESubRole subRoleName;
  private Integer numberOfCollection;
  private Integer numberOfClothes;
  private Integer price;
}
