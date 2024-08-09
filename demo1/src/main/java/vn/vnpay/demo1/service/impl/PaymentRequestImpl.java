package vn.vnpay.demo1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
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
        try {
            Bank bank = bankConfig.getBankByCode(paymentRequest.getBankCode());
            if (null == bank) {
                // todo:  khi dung moi nen khoi tao bien (short live) va bien (long live)
                // bien khoi tao nen dat ten (ex: "o2", ... )
                // khi nao nen dung log.error va log.info
                // su dung try catch, phan biet exception va throwable
                Response response = generateResponse("02", "Invalid bankCode", null);
                log.info("Invalid bankCode with response: {}", response);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            if (!validateRequest(paymentRequest)){
                Response response = generateResponse("01", "Item cannot be null or empty", bank);
                log.info("Item cannot be null or empty with response: {}", response);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            String data = paymentRequest.getMobile() + paymentRequest.getBankCode() + paymentRequest.getAccountNo() +
                    paymentRequest.getPayDate() + paymentRequest.getDebitAmount() + paymentRequest.getRespCode() +
                    paymentRequest.getTraceTransfer() + paymentRequest.getMessageType() + bank.getPrivateKey();

            String calculatedCheckSum = CheckSumUtil.sha256(data);
            // todo: bỏ sung ghi log checksum
            log.info("Calculated checksum: {}", calculatedCheckSum);
            if(!calculatedCheckSum.equals(paymentRequest.getCheckSum())){
                Response response = generateResponse("03","Invalid checkSum", bank);
                log.info("Invalid Check Sum with response: {}", response);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            Response response = generateResponse( "00","Payment successful", bank);
            log.info("End process payment with response: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("An error occurred: {}", ex.getMessage(), ex);
            Response response = generateResponse("05", "General error occurred", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } catch (Throwable th) {
            log.error("A critical error occurred: {}", th.getMessage(), th);
            Response response = generateResponse("06", "Critical error occurred", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private Response generateResponse(String code, String message, Bank bank) {
        // todo : su dung builder
        String responseId = UUID.randomUUID().toString();
        String responseTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return Response.builder()
                .code(code)
                .message(message)
                .responseId(responseId)
                .responseTime(responseTime)
                .checkSum(bank == null ? null : CheckSumUtil.sha256(
                                code + message + responseId +
                                        responseTime + bank.getPrivateKey()))
                .build();
    }

    private Boolean validateRequest(PaymentRequest request) throws IllegalArgumentException {
        if (request.getTokenKey() == null || request.getTokenKey().trim().isEmpty()) {
            // todo: true/ boolean.true and false/ boolean.false
            // bo sung log
            // check dao thu tu null va empty
            log.info("Invalid token key");
            return false;
        }
        if (request.getApiID() == null || request.getApiID().trim().isEmpty()) {
            log.info("Invalid API ID");
            return false;
        }
        if (request.getMobile() == null || request.getMobile().trim().isEmpty()) {
            log.info("Invalid mobile number");
            return false;
        }
        if (request.getBankCode() == null || request.getBankCode().trim().isEmpty()) {
            log.info("Invalid bank code");
            return false;
        }
        if (request.getAccountNo() == null || request.getAccountNo().trim().isEmpty()) {
            log.info("Invalid account number");
            return false;
        }
        if (request.getPayDate() == null || request.getPayDate().trim().isEmpty()) {
            log.info("Invalid payment date");
            return false;
        }
        if (request.getAdditionalData() == null || request.getAdditionalData().trim().isEmpty()) {
            log.info("Invalid additional data");
            return false;
        }
        if (request.getDebitAmount() == null) {
            log.info("Invalid debit amount");
            return false;
        }
        if (request.getRespDesc() == null || request.getRespDesc().trim().isEmpty()) {
            log.info("Invalid resp description");
            return false;
        }
        if (request.getRespCode() == null || request.getRespCode().trim().isEmpty()) {
            log.info("Invalid resp code");
            return false;
        }
        if (request.getTraceTransfer() == null || request.getTraceTransfer().trim().isEmpty()) {
            log.info("Invalid trace transfer");
            return false;
        }
        if (request.getMessageType() == null || request.getMessageType().trim().isEmpty()) {
            log.info("Invalid message type");
            return false;
        }
        if (request.getCheckSum() == null || request.getCheckSum().trim().isEmpty()) {
            log.info("Invalid check sum");
            return false;
        }
        if (request.getOrderCode() == null || request.getOrderCode().trim().isEmpty()) {
            log.info("Invalid order code");
            return false;
        }
        if (request.getUserName() == null || request.getUserName().trim().isEmpty()) {
            log.info("Invalid user name");
            return false;
        }
        if (request.getRealAmount() == null || request.getRealAmount().trim().isEmpty()) {
            log.info("Invalid real amount");
            return false;
        }
        if (request.getPromotionCode() == null || request.getPromotionCode().trim().isEmpty()) {
            log.info("Invalid promotion code");
            return false;
        }
        return true;
    }
}
