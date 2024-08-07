package vn.vnpay.demo1.model;

import lombok.Data;

@Data
public class ErrorMessage {
    private String message;
    private String code;

    public ErrorMessage(String message, String code) {
        this.message = message;
        this.code = code;
    }
}
