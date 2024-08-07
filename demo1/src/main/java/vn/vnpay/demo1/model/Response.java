package vn.vnpay.demo1.model;

import lombok.Data;

@Data
public class Response {
    private String code;
    private String message;
    private String responseId;
    private String responseTime;
    private String checkSum;

    public Response(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
