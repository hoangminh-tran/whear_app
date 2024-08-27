package com.tttm.Whear.App.service;

import com.tttm.Whear.App.exception.CustomException;

import java.util.List;

public interface PagingService {
    <T> List<T> paging(Integer page, Integer pageSize, List<T> list) throws CustomException;
}
