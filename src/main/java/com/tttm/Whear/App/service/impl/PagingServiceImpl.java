package com.tttm.Whear.App.service.impl;

import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.service.PagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PagingServiceImpl implements PagingService {
    @Override
    public <T> List<T> paging(Integer page, Integer pageSize, List<T> list) throws CustomException {
        try {

            if (page < 0) {
                throw new CustomException("Page can not be nagative!");
            }
            if (pageSize < 0) {
                throw new CustomException("PageSize can not be nagative!");
            }

            List<T> responseList = new ArrayList<>();
            Integer listSize = list.size();

            if (listSize <= pageSize) {
                return list;
            }

            Integer maxPage = (listSize / pageSize) + (listSize % pageSize == 0 ? 0 : 1);
            if (page > maxPage) {
                throw new CustomException("Their is no more Data!");
            }
            for (int index = (page * pageSize); index < ((page + 1) * pageSize); index++) {
                if (index < listSize) {
                    responseList.add(list.get(index));
                }
            }
            return responseList;
        } catch (Exception ex) {
            throw ex;
        }
    }
}
