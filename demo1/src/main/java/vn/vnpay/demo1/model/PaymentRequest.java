package vn.vnpay.demo1.model;

import lombok.Data;


@Data
public class PaymentRequest {
    private String tokenKey;
    private String apiID;
    private String mobile;
    private String bankCode;
    private String accountNo;
    private String payDate;
    private String additionalData;
    private Integer debitAmount;
    private String respDesc;
    private String respCode;
    private String traceTransfer;
    private String messageType;
    private String checkSum;
    private String orderCode;
    private String userName;
    private String realAmount;
    private String promotionCode;

}
