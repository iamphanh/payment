package vn.vnpay.demo1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import vn.vnpay.demo1.config.BankConfig;
import vn.vnpay.demo1.model.Bank;
import vn.vnpay.demo1.model.PaymentRequest;
import vn.vnpay.demo1.model.Response;
import vn.vnpay.demo1.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.vnpay.demo1.util.CheckSumUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Service
@EnableConfigurationProperties
public class PaymentRequestImpl implements PaymentService {

    @Autowired
    private BankConfig bankConfig;


    @Override
    public ResponseEntity<Response> payment(PaymentRequest paymentRequest) {
        log.info("Begin process payment with request: {}", paymentRequest);
        System.out.println("bankConfig: " + bankConfig.getBanks());
        Bank bank = bankConfig.getBankByCode(paymentRequest.getBankCode());
        if (bank == null) {
            Response response = generateResponse("02", "Invalid bankCode", null);
            log.error("Invalid bankCode with response: {}", response);
            return ResponseEntity.status(400).body(response);
        }
        if (!validateRequest(paymentRequest)){
            Response response = generateResponse("01", "Item cannot be null or empty", bank);
            log.error("Item cannot be null or empty with response: {}", response);
            return ResponseEntity.status(400).body(response);
        }
        String data = paymentRequest.getMobile() + paymentRequest.getBankCode() + paymentRequest.getAccountNo() +
                paymentRequest.getPayDate() + paymentRequest.getDebitAmount() + paymentRequest.getRespCode() +
                paymentRequest.getTraceTransfer() + paymentRequest.getMessageType() + bank.getPrivateKey();

        String calculatedCheckSum = CheckSumUtil.sha256(data);
        if(!calculatedCheckSum.equals(paymentRequest.getCheckSum())){
            Response response = generateResponse("03","Invalid checkSum", bank);
            log.error("Invalid Check Sum with response: {}", response);
            return ResponseEntity.status(400).body(response);
        }
        Response response = generateResponse( "00","Payment successful", bank);
        log.info("End process payment with response: {}", response);
        return ResponseEntity.ok(response);
    }

    private Response generateResponse(String code, String message, Bank bank) {
        Response response = new Response();
        response.setCode(code);
        response.setMessage(message);
        response.setResponseId(UUID.randomUUID().toString());
        response.setResponseTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        if(bank != null){
            String checkSum = response.getCode() + response.getMessage() + response.getResponseId() +
                    response.getResponseTime() + bank.getPrivateKey();
            response.setCheckSum(CheckSumUtil.sha256(checkSum));
        }
        return response;
    }

    private Boolean validateRequest(PaymentRequest request) throws IllegalArgumentException {
        if (request.getTokenKey() == null || request.getTokenKey().trim().isEmpty()) {
            return Boolean.FALSE;
        }
        if (request.getApiID() == null || request.getApiID().trim().isEmpty()) {
            return Boolean.FALSE;
        }
        if (request.getMobile() == null || request.getMobile().trim().isEmpty()) {
            return Boolean.FALSE;
        }
        if (request.getBankCode() == null || request.getBankCode().trim().isEmpty()) {
            return Boolean.FALSE;
        }
        if (request.getAccountNo() == null || request.getAccountNo().trim().isEmpty()) {
            return Boolean.FALSE;
        }
        if (request.getPayDate() == null || request.getPayDate().trim().isEmpty()) {
            return Boolean.FALSE;
        }
        if (request.getAdditionalData() == null || request.getAdditionalData().trim().isEmpty()) {
            return Boolean.FALSE;
        }
        if (request.getDebitAmount() == null) {
            return Boolean.FALSE;
        }
        if (request.getRespDesc() == null || request.getRespDesc().trim().isEmpty()) {
            return Boolean.FALSE;
        }
        if (request.getRespCode() == null || request.getRespCode().trim().isEmpty()) {
            return Boolean.FALSE;
        }
        if (request.getTraceTransfer() == null || request.getTraceTransfer().trim().isEmpty()) {
            return Boolean.FALSE;
        }
        if (request.getMessageType() == null || request.getMessageType().trim().isEmpty()) {
            return Boolean.FALSE;
        }
        if (request.getCheckSum() == null || request.getCheckSum().trim().isEmpty()) {
            return Boolean.FALSE;
        }
        if (request.getOrderCode() == null || request.getOrderCode().trim().isEmpty()) {
            return Boolean.FALSE;
        }
        if (request.getUserName() == null || request.getUserName().trim().isEmpty()) {
            return Boolean.FALSE;
        }
        if (request.getRealAmount() == null || request.getRealAmount().trim().isEmpty()) {
            return Boolean.FALSE;
        }
        if (request.getPromotionCode() == null || request.getPromotionCode().trim().isEmpty()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
