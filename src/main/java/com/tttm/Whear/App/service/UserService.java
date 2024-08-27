package com.tttm.Whear.App.service;

import com.tttm.Whear.App.entity.Customer;
import com.tttm.Whear.App.entity.User;
import com.tttm.Whear.App.enums.StatusGeneral;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.utils.request.StyleAndBodyShapeRequest;
import com.tttm.Whear.App.utils.request.UserRequest;
import com.tttm.Whear.App.utils.response.CustomerResponse;
import com.tttm.Whear.App.utils.response.UserResponse;
import java.util.List;
import java.util.Optional;

public interface UserService {

  CustomerResponse createNewUsers(UserRequest userRequest) throws CustomException;

  List<UserResponse> getUserbyUsername(String username) throws CustomException;
  UserResponse getUserbyUserID(String userid) throws CustomException;

  List<UserResponse> getAllUser() throws CustomException;

  UserResponse getUserByUserEmailAndPassword(String email, String password)
      throws CustomException;

  UserResponse updateUserByUserID(UserRequest userRequest) throws CustomException;

  UserResponse updateStatusUser(String userID) throws CustomException;

  User getUserEntityByUserID(String userID) throws CustomException;

  User registerNewUsers(UserRequest userRequest) throws CustomException;

  List<User> getAllUserEntity() throws CustomException;

  UserResponse convertToUserResponse(User user);

  CustomerResponse convertToCustomerResponse(User user, Customer customer);

  User getUserByEmail(String email) throws CustomException;

  Optional<User> findUserByEmailAndActiveStatus(String email, StatusGeneral status);
  void updateBodyShapeToUser(User user, String bodyShapeName) throws CustomException;

}
