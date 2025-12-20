package com.app.carpolling.controller;

import com.app.carpolling.dto.ApiResponse;
import com.app.carpolling.dto.PaymentRequest;
import com.app.carpolling.entity.Payment;
import com.app.carpolling.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PaymentController {
    
    private final PaymentService paymentService;
    
    @PostMapping("/process")
    public ResponseEntity<ApiResponse<Payment>> processPayment(
        @Valid @RequestBody PaymentRequest request
    ) {
        try {
            Payment payment = paymentService.processPayment(request);
            if (payment.getStatus().toString().equals("SUCCESS")) {
                return ResponseEntity.ok(
                    ApiResponse.success("Payment processed successfully", payment)
                );
            } else {
                return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                    .body(ApiResponse.error("Payment failed: " + payment.getFailureReason()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<ApiResponse<Payment>> getPaymentByBookingId(
        @PathVariable Long bookingId
    ) {
        try {
            Payment payment = paymentService.getPaymentByBookingId(bookingId);
            return ResponseEntity.ok(
                ApiResponse.success("Payment retrieved successfully", payment)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<ApiResponse<Payment>> getPaymentByTransactionId(
        @PathVariable String transactionId
    ) {
        try {
            Payment payment = paymentService.getPaymentByTransactionId(transactionId);
            return ResponseEntity.ok(
                ApiResponse.success("Payment retrieved successfully", payment)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(e.getMessage()));
        }
    }
}









