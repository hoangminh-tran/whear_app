package com.tttm.Whear.App.utils.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class NewsResponse implements Serializable {
    private Integer newsID;
    private BrandResponse brand;
    private String title;
    private String content;
    private String typeOfNews;
    private List<String> image;
    private String status;
    private String date;
}
