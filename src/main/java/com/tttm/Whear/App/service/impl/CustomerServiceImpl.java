package com.tttm.Whear.App.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tttm.Whear.App.entity.Customer;
import com.tttm.Whear.App.entity.User;
import com.tttm.Whear.App.enums.ESubRole;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.repository.BrandRepository;
import com.tttm.Whear.App.repository.CustomerRepository;
import com.tttm.Whear.App.repository.SubRoleRepository;
import com.tttm.Whear.App.service.CustomerService;
import com.tttm.Whear.App.service.PaymentService;
import com.tttm.Whear.App.service.UserService;
import com.tttm.Whear.App.utils.request.PaymentItem;
import com.tttm.Whear.App.utils.request.PaymentRequest;
import com.tttm.Whear.App.utils.request.UpdateBrandRequest;
import com.tttm.Whear.App.utils.response.BrandResponse;
import com.tttm.Whear.App.utils.response.PaymentResponse;
import com.tttm.Whear.App.utils.response.UpdateBrandResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
    private final CustomerRepository customerRepository;
    private final SubRoleRepository subRoleRepository;
    private final BrandRepository brandService;
    private final PaymentService paymentService;

    @Override
    @Caching(
            evict = @CacheEvict(cacheNames = "customers", allEntries = true),
            cacheable = @Cacheable(cacheNames = "customer", key = "#user.userID", condition = "#user.userID != null", unless = "#result == null")
    )
    public Customer createNewCustomers(User user) {
        return customerRepository.save(Customer
                .builder()
                .customerID(user.getUserID())
                .isFirstLogin(true)
                .subRoleID(subRoleRepository.getSubRolesBySubRoleName(ESubRole.LV1).getSubRoleID())
                .numberOfUpdateStyle(0)
                .build());
    }

    @Cacheable(cacheNames = "customer", key = "#customerID", condition = "#customerID != null", unless = "#result == null")
    @Override
    public Customer getCustomerByID(String customerID) {
        return customerRepository.getReferenceById(customerID);
    }

    @Override
    public void updateNumberOfTimesChangeStyleByCustomerID(Integer numberOfUpdateStyle, String customerID) {
        customerRepository.updateNumberOfTimesChangeStyleByCustomerID(numberOfUpdateStyle, customerID);
    }

    @Override
    public UpdateBrandResponse updateToBrand(UpdateBrandRequest updateBrandRequest) throws CustomException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        BrandResponse brandResponse = BrandResponse
                .builder()
                .brandName(updateBrandRequest.getBrandName())
                .brandID(updateBrandRequest.getCustomerID())
                .address(updateBrandRequest.getAddress())
                .description(updateBrandRequest.getDescription())
                .link(updateBrandRequest.getLink())
                .build();
        brandService.createNewBrand(
                updateBrandRequest.getCustomerID(),
                updateBrandRequest.getBrandName(),
                updateBrandRequest.getDescription(),
                updateBrandRequest.getLink(),
                updateBrandRequest.getAddress()
        );
        List<PaymentItem> item = new ArrayList<>();
        item.add(
                PaymentItem
                        .builder()
                        .quantity(1)
                        .price(159000)
                        .name("BRAND")
                        .build()
        );
        PaymentResponse paymentResponse = paymentService.createPayment(
                PaymentRequest
                        .builder()
                        .customerID(updateBrandRequest.getCustomerID())
                        .items(item)
                        .description(updateBrandRequest.getDescription())
                        .build()
        );
        return UpdateBrandResponse
                .builder()
                .brandID(brandResponse.getBrandID())
                .brandName(brandResponse.getBrandName())
                .address(brandResponse.getAddress())
                .link(brandResponse.getLink())
                .description(brandResponse.getDescription())
                .baseCustomer(null)
                .paymentInfor(paymentResponse)
                .build();
    }
}
