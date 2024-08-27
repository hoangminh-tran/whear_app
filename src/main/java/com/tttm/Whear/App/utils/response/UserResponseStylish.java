package com.tttm.Whear.App.utils.response;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode
public class UserResponseStylish {
    private String userID;
    private String username;
    private String imgUrl;
}
