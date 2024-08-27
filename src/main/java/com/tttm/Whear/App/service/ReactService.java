package com.tttm.Whear.App.service;

import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.utils.request.ReactRequest;
import com.tttm.Whear.App.utils.response.ReactResponse;
import java.util.List;

public interface ReactService {

  public ReactResponse unSendReact(ReactRequest reactRequest) throws CustomException;

  public List<ReactResponse> getPostReact(Integer postID) throws CustomException;

  public List<ReactResponse> getClothesReact(ReactRequest reactRequest) throws CustomException;

  public ReactResponse checkContain(Integer postID, String userID) throws CustomException;

  Integer getAllReactPerClothes(Integer postID) throws CustomException;
}
