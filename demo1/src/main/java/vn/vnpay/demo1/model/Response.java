package vn.vnpay.demo1.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Response {
    private String code;
    private String message;
    private String responseId;
    private String responseTime;
    private String checkSum;
}
