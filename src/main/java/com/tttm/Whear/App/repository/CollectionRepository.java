package com.tttm.Whear.App.repository;

import com.tttm.Whear.App.entity.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Integer> {

  @Query(nativeQuery = true, value = "select * from collection where collection.userID = ?1")
  public List<Collection> getAllByUserID(String userID);

  public Collection findByCollectionID(Integer collectionID);
}
