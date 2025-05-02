package com.boost.voucherAssessment.Controller;

import com.boost.voucherAssessment.Model.HttpResponse;
import com.boost.voucherAssessment.Service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/vouchers")
public class VoucherController {
    @Autowired
    private VoucherService voucherService;

    // Generate Vouchers
    @PostMapping("/generate")
    public ResponseEntity<HttpResponse> generate(@RequestBody GenerateRequest req) {
        HttpResponse response = new HttpResponse();

        try {
            response = voucherService.generateVouchers(req.getOfferId(), req.getExpirationDate());
            // Return a success response with message and HTTP status code
            return ResponseEntity.status(response.getStatus()).body(response);
        } catch (Exception e) {
            // Return an error response if something goes wrong
            response.setStatus(500);
            response.setMessage(e.getMessage());
            return ResponseEntity.status(response.getStatus()).body(response);
        }
    }

    // Validate Voucher
    @PostMapping("/validate")
    public ResponseEntity<HttpResponse> validate(@RequestBody ValidateRequest req) {
        HttpResponse response = new HttpResponse();

        try {
            // Attempt to validate the voucher and return the discount percentage
            response = voucherService.validateVoucher(req.getCode(), req.getEmail());
            return ResponseEntity.status(response.getStatus()).body(response);

        } catch (Exception e) {
            // Catch any other unexpected errors
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(response.getStatus()).body(response);

        }
    }

    // Get Valid Vouchers for a user
    @GetMapping("/valid")
    public ResponseEntity<HttpResponse> listValid(@RequestParam String email) {
        HttpResponse response = new HttpResponse();

        try {
            // Retrieve valid vouchers for the email
            response = voucherService.getValidVouchers(email);
            return ResponseEntity.status(response.getStatus()).body(response);

        } catch (Exception e) {
            // Return an error response if something goes wrong
            // Catch any other unexpected errors
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(response.getStatus()).body(response);

        }
    }


    public static class GenerateRequest {
        Long offerId;
        Date expirationDate;

        public Long getOfferId() {
            return offerId;
        }

        public void setOfferId(Long offerId) {
            this.offerId = offerId;
        }

        public Date getExpirationDate() {
            return expirationDate;
        }

        public void setExpirationDate(Date expirationDate) {
            this.expirationDate = expirationDate;
        }

        @Override
        public String toString() {
            return "GenerateRequest{" +
                    "offerId=" + offerId +
                    ", expirationDate=" + expirationDate +
                    '}';
        }
    }

    public static class ValidateRequest {
        String code;
        String email;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

}
