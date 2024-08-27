package com.tttm.Whear.App.utils.response;

import com.tttm.Whear.App.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateBrandResponse {
    private String brandID;
    private String brandName;
    private String description;
    private String address;
    private String link;
    private UserResponse baseCustomer;
    private PaymentResponse paymentInfor;
}
