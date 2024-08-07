package vn.vnpay.demo1.service;

import org.springframework.http.ResponseEntity;
import vn.vnpay.demo1.model.PaymentRequest;
import vn.vnpay.demo1.model.Response;

public interface PaymentService {
    ResponseEntity<Response> payment(PaymentRequest paymentRequest);
}
