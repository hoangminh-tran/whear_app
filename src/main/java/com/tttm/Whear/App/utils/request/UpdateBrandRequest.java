package com.tttm.Whear.App.utils.request;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class UpdateBrandRequest implements Serializable {
    private String customerID;
    private String brandName;
    private String address;
    private String description;
    private String link;
}
