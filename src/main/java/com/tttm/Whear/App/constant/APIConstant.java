package com.tttm.Whear.App.constant;

public class APIConstant {

  /**
   * Default API
   */
  public static final String API = "/api/v1";

  public class CollectionAPI {

    /**
     * Collection API
     */
    public static final String COLLECTION = APIConstant.API + "/collection";
    public static final String GET_ALL_COLLECTION_BY_USER_ID = "/get-all-by-userid";
    public static final String GET_COLLECTION_BY_ID = "/get-collection-by-id";
    public static final String UPDATE_COLLECTION_BY_ID = "/update-collection-by-id";
    public static final String DELETE_COLLECTION_BY_ID = "/delete-collection-by-id";
    public static final String CREATE_COLLECTION = "/create-collection";
  }

  public class UserAPI {

    /**
     * User API
     */
    public static final String USER = APIConstant.API + "/user";
    public static final String GET_ALL_USER = "/get-all-user";
    public static final String GET_USER_BY_USERNAME = "/get-user-by-username";
    public static final String GET_USER_BY_USERID = "/get-user-by-userid";
    public static final String GET_USER_BY_EMAIL_AND_PASSWORD = "/get-user-by-email-and-password";
    public static final String UPDATE_USER_BY_USERID = "/update-user-by-userid";
    public static final String CREATE_NEW_USER = "/create-new-user";
    public static final String UPDATE_STATUS_USER = "/update-status-user";

  }

  public class PostAPI {

    /**
     * Post API
     */
    public static final String POST = APIConstant.API + "/post";
    public static final String CREATE_POST = "/create-post";
    public static final String GET_POST_BY_POST_ID = "/get-post-by-postid";
    public static final String GET_ALL_POST = "/get-all-post";
    public static final String GET_POST_BY_TYPE_OF_POST = "/get-post-by-type-of-post";
    public static final String GET_POST_BY_HASHTAG = "/get-post-by-hashtag";
    public static final String GET_POST_IN_RANGE = "/get-post-in-range";
    public static final String DELETE_POST_BY_POSTID = "/delete-by-postid";
    public static final String UPDATE_POST = "/update-post";
    public static final String GET_ALL_POST_FOR_A_USER = "/get-all-post-for-user";
    public static final String GET_ALL_POST_OF_A_USER = "/get-all-post-of-user";

  }

  public class FollowAPI {

    /**
     * Follow API
     */
    public static final String FOLLOW = APIConstant.API + "/follow";
    public static final String USER_FOLLOW_OR_UNFOLLOW_ANOTHER_USER = "/un-follow";
    public static final String GET_ALL_FOLLOWER_USER = "/get-all-follower-user";
    public static final String GET_ALL_FOLLOWING_USER = "/get-all-following-user";
    public static final String GET_ALL_NOT_YET_FOLLOWING_USER = "/get-all-notyet-following-user";

    public static final String GET_NUMBER_OF_FOLLOWER_BY_USER_ID = "/get-number-of-follower-by-userid";
    public static final String GET_NUMBER_OF_FOLLOWING_BY_USER_ID = "/get-number-of-following-by-userid";
  }

  public class CustomerAPI {

    /**
     * Customer API
     */
    public static final String FOLLOW = APIConstant.API + "/customer";
  }

  public class ClothesAPI {

    /**
     * Clothes API
     */
    public static final String CLOTHES = APIConstant.API + "/clothes";
    public static final String CREATE_CLOTHES = "/create-clothes";
    public static final String GET_ALL_CLOTHES = "/get-all-clothes";
    public static final String GET_CLOTHES_BY_TYPE_OF_CLOTHES = "/get-clothes-by-type_of_clothes";
    public static final String GET_CLOTHES_BY_STYLE = "/get-clothes-by-style";
    public static final String GET_CLOTHES_BY_USERID = "/get-clothes-by-user-id";
    public static final String GET_CLOTHES_BY_ID = "/get-clothes-by-id";
    public static final String ADD_CLOTHES_TO_COLLECTION = "/add-clothes-to-collection";
    public static final String UPDATE_CLOTHES = "/update-clothes";
    public static final String DELETE_CLOTHES = "/delete-clothes";
  }

  public class NotificationAPI {

    /**
     * Notification API
     */
    public static final String NOTIFICATION = APIConstant.API + "/notification";
    public static final String GET_ALL_NOTIFICATION = "/get-all-notification";
    public static final String GET_UNREAD_NOTIFICATION = "/get-unread-notification";
    public static final String UN_READ_NOTIFICATION = "/un-read-notification";
  }

  public class HistoryAPI {

    public static final String HISTORY = APIConstant.API + "/histories";
    public static final String CREATE_HISTORY_ITEM = "/create-history-item";
    public static final String GET_ALL_HISTORY_ITEMS_BY_CUSTOMER_ID = "/get-all-history-items-by-customer-id";
    public static final String SUB_HTTP_OF_HISTORY = "/histories/**";
  }

  public class GenerateDataAPI {

    public static final String GENERATE_DATA = APIConstant.API + "/generate-data";

    public static final String GENERATE_RANDOM_LIST_CLOTHES = "/generate-random-list-clothes";
    public static final String GENERATE_RANDOM_HISTORY_USER_SEARCH = "/generate-random-history-user-search";
  }

  public class RecommendationAPI {

    public static final String RECOMMENDATION = APIConstant.API + "/recommedation";

    public static final String GET_LIST_RECOMMMENDATION_BY_USER_HISTORY_ITEMS = "/get-list-recommendation-by-user-history-items";
    public static final String GET_LIST_RECOMMENDATION_WHEN_FOLLOW_ANOTHER_USER = "/get-list-recommendation-when-follow-another-user";
    public static final String GET_LIST_RECOMMENDATION_BY_KEYWORD = "/get-list-recommendation-by-keyword";
  }

  public class AuthenticationAPI {

    public static final String AUTHENTICATION = APIConstant.API + "/auth";
    public static final String REGISTER = "/register";
    public static final String LOGIN = "/login";
    public static final String LOG_OUT = AUTHENTICATION + "/log-out";
    public static final String REFRESH_TOKEN = "/refresh-token";
  }

  public class ReactAPI {

    public static final String REACT = APIConstant.API + "/react";
    public static final String UN_SEND_REACT = "/un-send-react";
    public static final String GET_ALL_REACT_OF_POST = "/get-react-of-post";
    public static final String GET_ALL_REACT_OF_CLOTHES = "/get-react-of-clothes";
  }

  public class SubroleAPI {

    public static final String SUBROLE = APIConstant.API + "/subrole";
    public static final String GET_SUBROLE_BY_ID = "/get_subrole_by_id";
    public static final String UPDATE_SUBROLE = "/update-subrole";
  }

  public class BrandAPI{
    public static final String BRAND = APIConstant.API + "/brand";
    public static final String CREATE_NEW_BRAND = "/create-new-brand";
    public static final String UPDATE_TO_BRAND = "/update-to-brand";
    public static final String GET_HOT_BRAND = "/get-hot-brand";
  }

  public class RuleMatchingClothesAPI {
    public static final String RULE_MATCHING_CLOTHES = APIConstant.API + "/rule-matching-clothes";
    public static final String CREATE_NEW_RULE = "/create-new-rules";
    public static final String GET_RULE_BY_ID = "/get-rule-by-id";
    public static final String GET_ALL_RULE = "/get-all-rule";
  }

  public class UserStyleAPI{
    public static final String USER_STYLE = APIConstant.API + "/user-style";
    public static final String CREATE_STYLE_AND_BODY_SHAPE = "/create-style-and-body-shape";
    public static final String UPDATE_STYLE_FOR_CUSTOMER = "/update-style-for-customer";
  }

  /**
   * PAYMENT API
   */
  public class PaymentAPI{
    public static final String PAYMENT = APIConstant.API + "/payment";
    public static final String CREATE_PAYMENT = "/create-payment";
    public static final String GET_PAYMENT_BY_ID = "/get-payment-infor";
    public static final String GET_ALL_PAYMENT = "/get-all-payment";
  }

  public class AIStylishAPI
  {
    public static final String AI_STYLISH_API = APIConstant.API + "/ai-stylish";
    public static final String GET_SUGGEST_CLOTHES_FOR_USER = "/get-suggest-clothes-for-user";
    public static final String RENEW_CLOTHES_AFTER_REJECT_FOR_PREMIUM_USER = "/renew-clothes-after-reject-for-premium-user";
    public static final String SELECT_CHOICE_WHEN_RUN_OUT_OF_OUTFITS_FOR_PREMIUM = "/select-choice-when-run-out-of-outfits-for-premium";
    public static final String CALCULATE_MAXIMUM_OUTFITS_CAN_GENERATE = "/calculate-maximum-outfits-can-generate";
  }

  public class CommentAPI
  {
    public static final String COMMENT = APIConstant.API + "/comment";
    public static final String CREATE_COMMENT = "/create-comment";
    public static final String GET_ALL_COMMENT_OF_A_POST = "/get-all-comment";
    public static final String DELETE_BY_COMMENT_ID = "/delete-comment";
  }

  public class ChartAPI
  {
    public static final String CHART = APIConstant.API + "/chart";
    public static final String LANGUAGE_CHART = "/language-chart";
    public static final String PAYMENT_CHART = "/payment-chart";
  }

  public class NewsAPI
  {
    public static final String NEWS = APIConstant.API + "/news";
    public static final String CREATE_NEWS = "/create-news";
    public static final String UPDATE_NEWS = "/update-news";
    public static final String DELETE_NEWS = "/delete-news";
    public static final String GET_NEWS_BY_ID = "/get-news-by-id";
    public static final String GET_NEWS_BY_BRAND_ID = "/get-news-by-brand_id";
    public static final String GET_NEWS_BY_TYPE_OF_NEWS = "/get-news-by-type-of-news";
    public static final String GET_ALL_NEWS = "/get-all-news";
  }

  public class PagingAPI
  {
    public static final String PAGING = APIConstant.API + "/paging";
    public static final String GET_PAGE = "/get-page";
  }
}
