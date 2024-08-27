package com.tttm.Whear.App.service.impl;

import com.tttm.Whear.App.constant.ConstantMessage;
import com.tttm.Whear.App.entity.Collection;
import com.tttm.Whear.App.entity.CollectionClothes;
import com.tttm.Whear.App.entity.Customer;
import com.tttm.Whear.App.entity.SubRole;
import com.tttm.Whear.App.enums.ERole;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.repository.CollectionClothesRepository;
import com.tttm.Whear.App.repository.CollectionRepository;
import com.tttm.Whear.App.repository.SubRoleRepository;
import com.tttm.Whear.App.service.*;
import com.tttm.Whear.App.utils.request.CollectionRequest;
import com.tttm.Whear.App.utils.response.ClothesResponse;
import com.tttm.Whear.App.utils.response.CollectionResponse;
import com.tttm.Whear.App.utils.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CollectionServiceImpl implements CollectionService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    CollectionRepository collectionRepository;
    @Autowired
    UserService userService;
    private final CustomerService customerService;
    private final SubRoleRepository subRoleRepository;
    private final CollectionClothesRepository collectionClothesRepository;
    private final ClothesService clothesService;
    private final ReactService reactService;

    @Override
//  @Cacheable(cacheNames = "collections", key = "#username", unless = "#result == null")
    public List<CollectionResponse> getCollectionsOfUser(String username) throws CustomException {
        try {
            if (username.isBlank() || username.isEmpty()) {
                logger.error(ConstantMessage.USERID_IS_EMPTY_OR_NOT_EXIST.getMessage());
                throw new CustomException(ConstantMessage.USERID_IS_EMPTY_OR_NOT_EXIST.getMessage());
            }
            UserResponse user = userService.convertToUserResponse(
                    userService.getUserEntityByUserID(username));
            if (user == null) {
                logger.warn(ConstantMessage.CANNOT_FIND_USER_BY_USERNAME.getMessage());
                throw new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERNAME.getMessage());
            }
            String userID = user.getUserID();
            List<Collection> collectionList = collectionRepository.getAllByUserID(userID);
            List<CollectionResponse> responseList = new ArrayList<>();
            for (Collection col : collectionList) {
                if (responseList == null) {
                    responseList = new ArrayList<>();
                }
                responseList.add(
                        getCollectionByCollectionID(col.getCollectionID())
                );
            }
            return responseList;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
//  @Cacheable(cacheNames = "collection", key = "#collectionID", unless = "#result == null")
    public CollectionResponse getCollectionByCollectionID(Integer collectionID)
            throws CustomException {
        CollectionResponse collectionResponse = null;
        Collection collection = collectionRepository.findByCollectionID(collectionID);
        if (collection != null) {

            List<ClothesResponse> clothesList = new ArrayList<>();
            List<CollectionClothes> collectionClothesList = collectionClothesRepository.getAllByCollectionID(
                    collectionID);
            if (collectionClothesList != null) {
                for (CollectionClothes cc : collectionClothesList) {
                    if (clothesList == null) {
                        clothesList = new ArrayList<>();
                    }
                    ClothesResponse response = clothesService.getClothesByID(
                            cc.getCollectionClothesKey().getClothesID()
                    );
                    response.setReacted(
                            reactService.checkContain(
                                    response.getClothesID(),
                                    collection.getUserID()
                            ) != null
                    );
                    clothesList.add(
                            response
                    );
                }
            }

            collectionResponse = CollectionResponse.builder()
                    .collectionID(collection.getCollectionID())
                    .userID(collection.getUserID())
                    .nameOfCollection(collection.getNameOfCollection())
                    .typeOfCollection(collection.getTypeOfCollection())
                    .numberOfClothes(collection.getNumberOfClothes())
                    .imgUrl(collection.getImgUrl())
                    .clothesList(clothesList)
                    .build();
        }
        return collectionResponse;
    }

    @Override
//  @Caching(
//          evict = @CacheEvict(cacheNames = "collections", key = "#collection.userID"),
//          put = @CachePut(cacheNames = "collection", key = "#collection.collectionID", unless = "#result == null")
//  )
    public CollectionResponse updateCollectionByID(CollectionRequest collection) {
        CollectionResponse collectionResponse = null;
        try {
            Collection newCollection = collectionRepository.findByCollectionID(
                    collection.getCollectionID());
            if (newCollection != null) {
                // update collection entity
                newCollection.setNameOfCollection(collection.getNameOfCollection());
                newCollection.setTypeOfCollection(collection.getTypeOfCollection());
                newCollection.setNumberOfClothes(collection.getNumberOfClothes());

                // update to db
                collectionRepository.save(newCollection);

                // generate response
                collectionResponse = CollectionResponse.builder()
                        .collectionID(newCollection.getCollectionID())
                        .nameOfCollection(newCollection.getNameOfCollection())
                        .typeOfCollection(newCollection.getTypeOfCollection())
                        .numberOfClothes(newCollection.getNumberOfClothes())
                        .collectionStatus(newCollection.getCollectionStatus())
                        .imgUrl(newCollection.getImgUrl())
                        .build();
            }
            return collectionResponse;
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
//  @Caching(
//          evict = {
//                  @CacheEvict(cacheNames = "collections", allEntries = true),
//                  @CacheEvict(cacheNames = "collection", key = "#collection.collectionID")
//          }
//  )
    public void deleteCollectionByID(Integer collectionID) {
        try {
            Collection collection = collectionRepository.findByCollectionID(collectionID);
            if (collection != null) {
                collectionClothesRepository.deleteByCollectionID(collectionID);
                collectionRepository.delete(collection);
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Override
//  @Caching(
//          evict = @CacheEvict(cacheNames = "collections", key = "#collection.userID", allEntries = true),
//          cacheable = @Cacheable(cacheNames = "collection", key = "#collection.collectionID", unless = "#result == null")
//  )
    public CollectionResponse createCollection(CollectionRequest collection) throws CustomException {
        try {
            String username = collection.getUserID();
            if (username.isBlank() || username.isEmpty()) {
                logger.error(ConstantMessage.USERID_IS_EMPTY_OR_NOT_EXIST.getMessage());
                throw new CustomException(ConstantMessage.USERID_IS_EMPTY_OR_NOT_EXIST.getMessage());
            }
            UserResponse user = userService.convertToUserResponse(
                    userService.getUserEntityByUserID(username));
            if (user == null) {
                logger.warn(ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage());
                throw new CustomException(ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage());
            }
            String userID = user.getUserID();
            if (user.getRole().equals(ERole.CUSTOMER)) {
                Customer customer = customerService.getCustomerByID(userID);
                SubRole subRole = subRoleRepository.getSubRolesBySubRoleID(customer.getSubRoleID());
                if (collectionRepository.getAllByUserID(userID).size() + 1
                        > subRole.getNumberOfCollection()) {
                    logger.error(ConstantMessage.REACH_MAXIMUM_COLLECTION.getMessage());
                    throw new CustomException(ConstantMessage.REACH_MAXIMUM_COLLECTION.getMessage());
                }
            }
            CollectionResponse response = null;
            Collection col = Collection.builder()
                    .collectionID(collection.getCollectionID())
                    .typeOfCollection(collection.getTypeOfCollection())
                    .nameOfCollection(collection.getNameOfCollection())
                    .numberOfClothes(collection.getNumberOfClothes())
                    .collectionStatus(collection.getCollectionStatus())
                    .userID(userID)
                    .imgUrl(collection.getImgUrl())
                    .build();
            Collection newCollection = collectionRepository.save(col);

            response = CollectionResponse.builder()
                    .collectionID(newCollection.getCollectionID())
                    .nameOfCollection(newCollection.getNameOfCollection())
                    .typeOfCollection(newCollection.getTypeOfCollection())
                    .numberOfClothes(newCollection.getNumberOfClothes())
                    .collectionStatus(newCollection.getCollectionStatus())
                    .imgUrl(newCollection.getImgUrl())
                    .build();
            return response;
        } catch (Exception ex) {
            throw ex;
        }
    }
}
