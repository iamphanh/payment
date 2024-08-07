package vn.vnpay.demo1.controller;

import org.springframework.http.ResponseEntity;
import vn.vnpay.demo1.model.PaymentRequest;
import vn.vnpay.demo1.model.Response;
import vn.vnpay.demo1.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @PostMapping("/payment")
    public ResponseEntity<Response> payment(@RequestBody PaymentRequest paymentRequest) {
        return paymentService.payment(paymentRequest);
    }
}
