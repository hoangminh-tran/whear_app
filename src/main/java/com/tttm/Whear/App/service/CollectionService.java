package com.tttm.Whear.App.service;

import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.utils.request.CollectionRequest;
import com.tttm.Whear.App.utils.response.CollectionResponse;
import java.util.List;

public interface CollectionService {

  public List<CollectionResponse> getCollectionsOfUser(String userID) throws CustomException;

  public CollectionResponse getCollectionByCollectionID(Integer collectionID)
      throws CustomException;

  public CollectionResponse updateCollectionByID(CollectionRequest collection);

  public void deleteCollectionByID(Integer collectionID);

  public CollectionResponse createCollection(CollectionRequest collection) throws CustomException;
}
