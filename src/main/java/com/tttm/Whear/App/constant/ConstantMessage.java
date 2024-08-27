package com.tttm.Whear.App.constant;

import lombok.Getter;

@Getter
public enum ConstantMessage {
  CREATE_SUCCESS("Create Success"),
  CREATE_FAIL("Create Fail"),
  INVALID_USERNAME_OR_PASSWORD("Invalid Username or Password"),
  CANNOT_FIND_USER_BY_USERNAME("Can not find User by Username"),
  ID_IS_EMPTY_OR_NOT_EXIST("Id is empty or not exist"),
  CANNOT_FIND_USER_BY_USERID("Can not find User by UserID"),
  USERID_IS_EMPTY_OR_NOT_EXIST("UserID is empty or not exist"),
  USER_EMAIL_IS_EMPTY_OR_NOT_EXIST("User Email is empty or not exist"),
  USERNAME_IS_EXIST("Username has existed already"),
  PHONE_IS_EXIST("Phone has existed already"),
  EMAIL_IS_EXIST("Email has existed already"),
  INVALID_ARGUMENT("Invalid argument"),
  MISSING_ARGUMENT("Some Arguments are missing or empty"),
  FORBIDDEN("Customer do not have role to access the page"),
  RESOURCE_NOT_FOUND("Resource not found"),
  REACH_MAXIMUM_COLLECTION("Please update plan to get more collection"),
  REQUIRED_UPDATE_PLAN("Update plan to use this feature"),
  HAVE_NEW_POST("Have A New Post!"),
  HAVE_NEW_FOLLOWER("Have A New Follower!"),
  CAN_NOT_FIND_STYLE_NAME("Can not find Style Name"),
  STYLE_ID_AND_USER_ID_IS_EXIST("StyleID and UserID is existed"),
  CAN_NOT_FIND_BODY_SHAPE_NAME("Can not find Body Shape Name"),
  USER_NOT_EXIST_IN_DATABASE("There is no user exist in database"),
  SUGGEST_FULL_CLOTHES_FOR_FREE_USER("Suggest full clothes for free user"),
  SUGGEST_MISSING_OR_RUN_OUT_OF_CLOTHES_FOR_FREE_USER("Suggest Missing or Run out of clothes for free user"),
  SUGGEST_FULL_CLOTHES_FOR_PREMIUM_USER("Suggest full clothes for Premium user"),
  SUGGEST_MISSING_OR_RUN_OUT_OF_CLOTHES_FOR_PREMIUM_USER("Suggest Missing or Run out of clothes for Premium user"),
  STYLE_NAME_IS_NOT_EXISTED("Style name is not existed in database"),
  STYLE_NAME_IS_EXIST("Style name is existed"),
  STYLE_NAME_IS_EXIST_IN_USER_STYLE("Style name is existed in User Style"),
  STYLE_NAME_IS_NOT_EXIST_IN_USER_STYLE("Style name is not existed in User Style"),
  ACCOUNT_HAVE_CHANGE_STYLE_MAXIMUM_NUMBER_OF_TIMES("Your Account have change style maximum number of Times"),
  OLD_STYLE_AND_NEW_STYLE_IS_THE_SAME("Old Style And New Style is the same"),
  BODY_SHAPE_NAME_IS_NOT_EXISTED("Body Shape Name is not existed");

  private final String message;

  ConstantMessage(String message) {
    this.message = message;
  }
}
