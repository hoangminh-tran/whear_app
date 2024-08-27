package com.tttm.Whear.App.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tttm.Whear.App.constant.ConstantMessage;
import com.tttm.Whear.App.entity.Customer;
import com.tttm.Whear.App.entity.Payment;
import com.tttm.Whear.App.entity.SubRole;
import com.tttm.Whear.App.enums.ESubRole;
import com.tttm.Whear.App.exception.CustomException;
import com.tttm.Whear.App.repository.CustomerRepository;
import com.tttm.Whear.App.repository.PaymentRepository;
import com.tttm.Whear.App.service.PaymentService;
import com.tttm.Whear.App.service.SubroleService;
import com.tttm.Whear.App.service.UserBucketService;
import com.tttm.Whear.App.utils.request.PaymentItem;
import com.tttm.Whear.App.utils.request.PaymentRequest;
import com.tttm.Whear.App.utils.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.Map.Entry;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Value("${PAYOS_CREATE_PAYMENT_LINK_URL}")
    private String createPaymentLinkUrl;
    @Value("${PAYOS_CLIENT_ID}")
    private String clientId;
    @Value("${PAYOS_API_KEY}")
    private String apiKey;
    @Value("${PAYOS_CHECKSUM_KEY}")
    private String checksumKey;

    private final CustomerRepository customerService;
    private final SubroleService subroleService;
    private final PaymentRepository paymentRepository;
    private final UserBucketService userBucketService;
    @Override
    public PaymentResponse createPayment(PaymentRequest paymentRequest)
            throws CustomException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String customerID = paymentRequest.getCustomerID();
        if (customerID == null) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }
        Customer customer = customerService.getReferenceById(customerID);
        if (customer == null) {
            throw new CustomException(
                    ConstantMessage.CANNOT_FIND_USER_BY_USERID.getMessage() + " " + customerID);
        }
        String buyerName = "";
        String buyerEmail = "";
        String buyerPhone = "";
        if (paymentRequest.getBuyerName() != null && !paymentRequest.getBuyerName().isEmpty() && !paymentRequest.getBuyerName().isBlank()) {
            buyerName = paymentRequest.getBuyerName().trim();
        } else {
            buyerName = customer.getUser().getUsername();
        }
        if (paymentRequest.getBuyerEmail() != null && !paymentRequest.getBuyerEmail().trim().isEmpty() && !paymentRequest.getBuyerEmail().trim().isBlank()) {
            buyerName = paymentRequest.getBuyerEmail().trim();
        } else {
            buyerEmail = customer.getUser().getEmail();
        }
        if (paymentRequest.getBuyerPhone() != null && !paymentRequest.getBuyerPhone().isEmpty() && !paymentRequest.getBuyerPhone().isBlank()) {
            buyerPhone = paymentRequest.getBuyerPhone().trim();
        } else {
            buyerPhone = customer.getUser().getPhone();
        }

        List<PaymentItem> items = paymentRequest.getItems();
        if (items == null || items.size() <= 0) {
            throw new CustomException(ConstantMessage.MISSING_ARGUMENT.getMessage());
        }
        if (!items.get(0).getName().trim().toUpperCase().equals("BRAND")) {
            SubRole subRole = subroleService.getSubroleBySubroleName(
                    ESubRole.valueOf(items.get(0).getName()));
            if (subRole == null) {
                throw new CustomException(ConstantMessage.RESOURCE_NOT_FOUND + " for SUBROLE: " + items);
            }

            PaymentItem item = items.get(0);
            item.setQuantity(
                    1
            );
            item.setPrice(
                    subRole.getPrice()
            );
            items.set(0, item);
        }

        Integer amount = items.get(0).getPrice();

        String currentTimeString = String.valueOf(String.valueOf(new Date().getTime()));
        Integer orderCode = Integer.parseInt(
                currentTimeString.substring(currentTimeString.length() - 6));

        String status = "PENDING";


        final String returnUrl = "https://whearapp.tech/payment-infor?payos=1&item="+items.get(0).getName().trim().toUpperCase();
        final String cancelUrl = "https://whearapp.tech/payment-cancel";
        paymentRequest.setBuyerEmail(buyerEmail);
        paymentRequest.setBuyerName(buyerName);
        paymentRequest.setBuyerPhone(buyerPhone);
        paymentRequest.setAmount(amount);
        paymentRequest.setOrderCode(orderCode);
        paymentRequest.setItems(items);
        paymentRequest.setReturnUrl(returnUrl);
        paymentRequest.setCancelUrl(cancelUrl);

        String bodyToSignature = createSignatureOfPaymentRequest(paymentRequest, checksumKey);
        paymentRequest.setSignature(bodyToSignature);

        // Tạo header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-client-id", clientId);
        headers.set("x-api-key", apiKey);
        // Gửi yêu cầu POST
        WebClient client = WebClient.create();
        Mono<String> response = client.post()
                .uri(createPaymentLinkUrl)
                .headers(httpHeaders -> httpHeaders.putAll(headers))
                .body(BodyInserters.fromValue(paymentRequest))
                .retrieve()
                .bodyToMono(String.class);
        String responseBody = response.block();
        JsonNode res = objectMapper.readTree(responseBody);
        System.out.println(res);
        if (!Objects.equals(res.get("code").asText(), "00")) {
            throw new CustomException("Fail");
        }
        String bin = res.get("data").get("bin").asText();
        String accountNumber = res.get("data").get("accountNumber").asText();
        String accountName = res.get("data").get("accountName").asText();
        String description = res.get("data").get("description").asText();
        String currency = res.get("data").get("currency").asText();
        String paymentLinkId = res.get("data").get("paymentLinkId").asText();
        status = res.get("data").get("status").asText();
        String checkoutUrl = res.get("data").get("checkoutUrl").asText();
        String qrCode = res.get("data").get("qrCode").asText();

        //Kiểm tra dữ liệu có đúng không
        String paymentLinkResSignature = createSignatureFromObj(res.get("data"), checksumKey);
//    System.out.println("RES: " + res);
//    System.out.println(paymentLinkResSignature);
        if (!paymentLinkResSignature.equals(res.get("signature").asText())) {
//      orderRepository.deleteOrderByOrderID(newestOrder.getId());
            throw new CustomException("Signature is not compatible");
        }

        PaymentResponse paymentResponse = PaymentResponse
                .builder()
                .code(res.get("code").asText())
                .desc("Success - Thành công")
                .data(PaymentData
                        .builder()
                        .bin(bin)
                        .accountNumber(accountNumber)
                        .accountName(accountName)
                        .amount(amount)
                        .description(description)
                        .orderCode(orderCode)
                        .currency(currency)
                        .paymentLinkId(paymentLinkId)
                        .status(status)
                        .checkoutUrl(checkoutUrl)
                        .qrCode(qrCode)
                        .build()
                )
                .signature(paymentRequest.getSignature())
                .build();
        Payment payment = Payment
                .builder()
                .paymentID(orderCode)
                .customerID(customerID)
                .value(amount)
                .status(status)
                .checkoutUrl(checkoutUrl)
                .qrCode(qrCode)
                .build();
        paymentRepository.save(payment);
        return paymentResponse;
    }

    @Override
    public PaymentInformation getPaymentInfor(String paymentID)
            throws CustomException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-client-id", clientId);
        headers.set("x-api-key", apiKey);
        // Gửi yêu cầu POST
        WebClient client = WebClient.create();
        Mono<String> response = client.get()
                .uri(createPaymentLinkUrl + "/" + paymentID)
                .headers(httpHeaders -> httpHeaders.putAll(headers))
                .retrieve()
                .bodyToMono(String.class);
        String responseBody = response.block();
        JsonNode res = objectMapper.readTree(responseBody);
        System.out.println(res);
        if (!Objects.equals(res.get("code").asText(), "00")) {
            throw new CustomException("Fail");
        }

        String code = res.get("code").asText();
        String desc = res.get("desc").asText();
        String id = res.get("data").get("id").asText();
        Integer orderCode = Integer.parseInt(res.get("data").get("orderCode").asText());
        Integer amount = Integer.parseInt(res.get("data").get("amount").asText());
        Integer amountPaid = Integer.parseInt(res.get("data").get("amountPaid").asText());
        Integer amountRemaining = Integer.parseInt(res.get("data").get("amountRemaining").asText());
        String status = res.get("data").get("status").asText();
        String createdAt = res.get("data").get("createdAt").asText();
        List<Transactions> transactions = new ArrayList<>();
        JsonNode jsonArrayNode = res.get("data").get("transactions");
        for (JsonNode jsonNode : jsonArrayNode) {
            Transactions transactionsObject = objectMapper.convertValue(jsonNode, Transactions.class);
            transactions.add(transactionsObject);
        }
        String canceledAt = res.get("data").get("canceledAt").asText();
        String cancellationReason = res.get("data").get("cancellationReason").asText();
        String signature = res.get("signature").asText();

        PaymentInformationData data = PaymentInformationData
                .builder()
                .id(id)
                .orderCode(orderCode)
                .amount(amount)
                .amountPaid(amountPaid)
                .amountRemaining(amountRemaining)
                .status(status)
                .createdAt(createdAt)
                .transactions(transactions)
                .canceledAt(canceledAt)
                .cancellationReason(cancellationReason)
                .build();

        Payment paymentObject = paymentRepository.getByPaymentID(orderCode);

        PaymentInformation paymentInformation = PaymentInformation
                .builder()
                .code(code)
                .desc(desc)
                .data(data)
                .signature(signature)
                .checkoutUrl(paymentObject.getCheckoutUrl())
                .qrCode(paymentObject.getQrCode())
                .build();
        return paymentInformation;
    }

    @Override
    public List<Payment> getAllPayment() throws Exception {
        List<Payment> paymentList = paymentRepository.findAll();
        return paymentList;
    }

    @Override
    public List<PaymentInformation> getAllPayment(String userId) throws Exception {
        List<Payment> paymentList = paymentRepository.getAllPayment(userId);
        List<PaymentInformation> response = new ArrayList<>();
        for(Payment p : paymentList){
            response.add(getPaymentInfor(p.getPaymentID().toString()));
        }
        return response;
    }

    @Override
    public String getDateTime(Integer paymentID) {
        return paymentRepository.getDateTime(paymentID);
    }

    @Override
    public void confirmUpdate(Integer orderCode, String item) throws CustomException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        PaymentInformation paymentInformation = getPaymentInfor(orderCode.toString());
        if(paymentInformation.getData().getStatus().equals("PAID")) {
            Payment payment = paymentRepository.getByPaymentID(orderCode);
            Customer customer = customerService.getReferenceById(payment.getCustomerID());
            // Renew API Call Outfits when upgrade for Free User to Premium User
            System.out.println("Before call storeCallDataWhenUpgradePremium");
            userBucketService.storeCallDataWhenUpgradePremium(payment.getCustomerID());
            customer.setSubRoleID(subroleService.getSubroleBySubroleName(ESubRole.valueOf(item)).getSubRoleID());
            customerService.save(customer);
        }
    }

    private static String convertObjToQueryStr(JsonNode object) {
        StringBuilder stringBuilder = new StringBuilder();
        ObjectMapper objectMapper = new ObjectMapper();

        object.fields().forEachRemaining(entry -> {
            String key = entry.getKey();
            JsonNode value = entry.getValue();
            String valueAsString = value.isTextual() ? value.asText() : value.toString();

            if (!stringBuilder.isEmpty()) {
                stringBuilder.append('&');
            }
            stringBuilder.append(key).append('=').append(valueAsString);
        });

        return stringBuilder.toString();
    }

    private static JsonNode sortObjDataByKey(JsonNode object) {
        if (!object.isObject()) {
            return object;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode orderedObject = objectMapper.createObjectNode();

        Iterator<Entry<String, JsonNode>> fieldsIterator = object.fields();
        TreeMap<String, JsonNode> sortedMap = new TreeMap<>();

        while (fieldsIterator.hasNext()) {
            Entry<String, JsonNode> field = fieldsIterator.next();
            sortedMap.put(field.getKey(), field.getValue());
        }

        sortedMap.forEach(orderedObject::set);

        return orderedObject;
    }

    private static String generateHmacSHA256(String dataStr, String key)
            throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256Hmac.init(secretKey);
        byte[] hmacBytes = sha256Hmac.doFinal(dataStr.getBytes(StandardCharsets.UTF_8));

        // Chuyển byte array sang chuỗi hex
        StringBuilder hexStringBuilder = new StringBuilder();
        for (byte b : hmacBytes) {
            hexStringBuilder.append(String.format("%02x", b));
        }
        return hexStringBuilder.toString();
    }

    public static String createSignatureFromObj(JsonNode data, String key)
            throws NoSuchAlgorithmException, InvalidKeyException {
        JsonNode sortedDataByKey = sortObjDataByKey(data);
        String dataQueryStr = convertObjToQueryStr(sortedDataByKey);
        return generateHmacSHA256(dataQueryStr, key);
    }

    public static String createSignatureOfPaymentRequest(PaymentRequest data, String key)
            throws NoSuchAlgorithmException, InvalidKeyException {
        int amount = data.getAmount();
        String cancelUrl = data.getCancelUrl();
        String description = data.getDescription();
        String orderCode = Integer.toString(data.getOrderCode());
        String returnUrl = data.getReturnUrl();
        String dataStr = "amount=" + amount + "&cancelUrl=" + cancelUrl + "&description=" + description
                + "&orderCode=" + orderCode + "&returnUrl=" + returnUrl;
//    String dataStr = "amount=" + amount + "&description=" + description
//        + "&orderCode=" + orderCode;
        // Sử dụng HMAC-SHA-256 để tính toán chữ ký
        return generateHmacSHA256(dataStr, key);
    }
}
