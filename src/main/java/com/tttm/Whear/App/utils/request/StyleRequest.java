package com.tttm.Whear.App.utils.request;

import com.tttm.Whear.App.enums.StyleType;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class StyleRequest {
    private String styleName;
}
