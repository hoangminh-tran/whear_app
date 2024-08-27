package com.tttm.Whear.App.service;

public interface UserBucketService {
    boolean canCallAPI(String userID);

    void storeCallDataAndUser(String userID, Object apiData);
    Object getOldDataByUserID(String userID);
    void storeCallDataWhenUpgradePremium(String userID);
}
