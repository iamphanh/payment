package vn.vnpay.demo1.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class PaymentRequest {
    @NotBlank
    private String tokenKey;
    @NotBlank
    private String apiID;
    @NotBlank
    private String mobile;
    @NotBlank
    private String bankCode;
    @NotBlank
    private String accountNo;
    @NotBlank
    private String payDate;
    @NotBlank
    private String additionalData;
    @NotNull
    private Integer debitAmount;
    @NotBlank
    private String respDesc;
    @NotBlank
    private String respCode;
    @NotBlank
    private String traceTransfer;
    @NotBlank
    private String messageType;
    @NotBlank
    private String checkSum;
    @NotBlank
    private String orderCode;
    @NotBlank
    private String userName;
    @NotBlank
    private String realAmount;
    @NotBlank
    private String promotionCode;

}
