package com.tttm.Whear.App.service.impl;

import com.tttm.Whear.App.service.PaymentService;
import com.tttm.Whear.App.service.UserBucketService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Service
@RequiredArgsConstructor
public class UserBucketServiceImpl implements UserBucketService {
    private Map<String, ZonedDateTime> userLastCallDate = new ConcurrentHashMap<>();
    private Map<String, Object> userCallData = new ConcurrentHashMap<>();
    private ZoneId utcZone = ZoneId.of("UTC");
    private final Logger logger = LoggerFactory.getLogger(UserBucketServiceImpl.class);

//    private final Map<String, Map<LocalDate, Object>> userCallData = new ConcurrentHashMap<>();

//    @Override
//    public boolean canCallAPI(String userID) {
//        Map<LocalDate, Object> userCalls = userCallData.get(userID);
//        if(userCalls != null && !userCalls.isEmpty())
//        {
//            // Get The Last Time User Call the API
//            LocalDate lastTimeCallByUser = userCalls.keySet().stream().max(LocalDate::compareTo).orElse(null);
//            if(lastTimeCallByUser != null)
//            {
//                LocalDate startOfCurrentWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
//                if(lastTimeCallByUser.isBefore(startOfCurrentWeek))
//                {
//                    return true;
//                }
//                else return false;
//            }
//        }
//        return true;
//    }
//
//    @Override
//    public void storeCallDataAndUser(String userID, Object apiData) {
//        Map<LocalDate, Object> userCallDateAndData = userCallData.computeIfAbsent(userID, k -> new ConcurrentHashMap<>());
//        LocalDate startOfCurrentWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
//        userCallDateAndData.put(startOfCurrentWeek, apiData);
//    }
//
//    @Override
//    public Object getOldDataByUserID(String userID) {
//        Map<LocalDate, Object> userCallDateAndData = userCallData.get(userID);
//        if(userCallDateAndData != null && !userCallDateAndData.isEmpty())
//        {
//            LocalDate startOfCurrentWeek = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
//            return userCallDateAndData.get(startOfCurrentWeek);
//        }
//        return null;
//    }
    @Override
    public boolean canCallAPI(String userID) {
        return !userLastCallDate.containsKey(userID) ||
                userLastCallDate.get(userID).plusDays(7).isBefore(ZonedDateTime.now(utcZone));
    }

    @Override
    public void storeCallDataAndUser(String userID, Object apiData) {
        userCallData.put(userID, apiData);
        userLastCallDate.put(userID, ZonedDateTime.now(utcZone));
    }

    @Override
    public Object getOldDataByUserID(String userID) {
        return userCallData.get(userID);
    }

    @Override
    public void storeCallDataWhenUpgradePremium(String userID) {
        if(userCallData.containsKey(userID)) {
            logger.warn("userCallData with UpgradePremium : {}", userID);
            userCallData.remove(userID);

        }
        if(userLastCallDate.containsKey(userID)) {
            logger.warn("userLastCallDate with UpgradePremium : {}", userID);
            userLastCallDate.remove(userID);
        }
    }
}
