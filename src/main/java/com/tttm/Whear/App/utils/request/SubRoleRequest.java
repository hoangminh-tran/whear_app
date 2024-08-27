package com.tttm.Whear.App.utils.request;

import com.tttm.Whear.App.enums.ESubRole;
import jakarta.persistence.Column;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class SubRoleRequest {
    private String subRoleName;
    private Integer numberOfCollection;
    private Integer numberOfClothes;
    private Integer price;
}
