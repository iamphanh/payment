package vn.vnpay.demo1.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import vn.vnpay.demo1.config.BankConfig;
import vn.vnpay.demo1.model.Bank;
import vn.vnpay.demo1.model.ErrorMessage;
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
public class PaymentRequestImpl implements PaymentService {

    private final BankConfig bankConfig;

    public PaymentRequestImpl(BankConfig bankConfig) {
        this.bankConfig = bankConfig;
    }

    @Override
    public ResponseEntity<Response> payment(PaymentRequest paymentRequest) {
        log.info("Begin process payment with request: {}", paymentRequest);

        Bank bank =bankConfig.getBankByCode(paymentRequest.getBankCode());
        if (bank == null) {
            log.error("Invalid bankCode");
            return ResponseEntity.status(400).body(new Response( "Invalid bankCode","02"));
        }
        String data = paymentRequest.getMobile() + paymentRequest.getBankCode() + paymentRequest.getAccountNo() +
                paymentRequest.getPayDate() + paymentRequest.getDebitAmount() + paymentRequest.getRespCode() +
                paymentRequest.getTraceTransfer() + paymentRequest.getMessageType() + bank.getPrivateKey();

        String calculatedCheckSum = CheckSumUtil.sha256(data);
        if(!calculatedCheckSum.equals(paymentRequest.getCheckSum())){
            log.error("Invalid Check Sum");
            return ResponseEntity.status(400).body(new Response( "Invalid checkSum","03"));
        }
        Response response = new Response( "Payment successful","01");
        log.info("End process payment with response: {}",generateResponse(bank, response));
        return ResponseEntity.ok(generateResponse(bank, response));
    }

    private Response generateResponse(Bank bank, Response response) {
        response.setResponseId(UUID.randomUUID().toString());
        response.setResponseTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        String checkSum = response.getCode() + response.getMessage() + response.getResponseId() +
                response.getResponseTime() + bank.getPrivateKey();
        response.setCheckSum(checkSum);
        return response;
    }
}
