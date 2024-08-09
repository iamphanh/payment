package vn.vnpay.demo1.model;

import lombok.Data;

@Data
public class Response {
    private String code;
    private String message;
    private String responseId;
    private String responseTime;
    private String checkSum;
}
