package com.tttm.Whear.App.utils.request;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class NewsRequest implements Serializable {
    private Integer newsID;
    private Integer brandID;
    private String title;
    private String content;
    private String typeOfNews;
    private List<String> image;
    private String status;
}
