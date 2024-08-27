package com.tttm.Whear.App.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserFollowDto {
    private String followingUserID;
    private Double consineSimilarity;
    private Long totalOfFollowerOfTargetUserID;
    private Long totalOfFollowingOfTargetUserID;

    @Override
    public String toString() {
        return "UserFollowDto{" +
                "targetUserID='" + followingUserID + '\'' +
                ", consineSimilarity=" + consineSimilarity +
                ", totalOfFollowerOfTargetUserID=" + totalOfFollowerOfTargetUserID +
                ", totalOfFollowingOfTargetUserID=" + totalOfFollowingOfTargetUserID +
                '}';
    }
}
