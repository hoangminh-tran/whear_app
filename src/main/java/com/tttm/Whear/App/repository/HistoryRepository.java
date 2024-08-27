package com.tttm.Whear.App.repository;

import com.tttm.Whear.App.entity.ClothesColor;
import com.tttm.Whear.App.entity.History;
import com.tttm.Whear.App.enums.ColorType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Integer> {
    @Modifying
    @Transactional
    @Query(value = "insert into history (customerid, history_item, history_index, create_date, last_modified_date) values (?1, ?2, ?3, current_timestamp, null)", nativeQuery = true)
    void createHistoryItem(String customerID, String historyItem, String index);

    @Transactional
    @Query(value = "select * from history where history.customerid = ?1", nativeQuery = true)
    List<History> getAllHistoryItemsByCustomerID(String customerID);

    @Modifying
    @Transactional
    @Query(value = "delete from history where customerid = ?1 and history_index = ?2", nativeQuery = true)
    Integer deleteAllHistoryItems(String userID, String historyIndex);

    @Modifying
    @Transactional
    @Query(value = "update history set history_item = ?1, last_modified_date = current_timestamp where  customerid = ?2 and history_item = ?3 and history_index = ?4", nativeQuery = true)
    void updateHistoryByNewStyle(String newStyleName, String customerID, String oldStyleName, String index);

    @Query(value = "select * from history where customerid = ?1 and history_index = ?2", nativeQuery = true)
    List<History> getAllHistoryItemByUserIDAnIndex(String customerID, String historyIndex);
}
