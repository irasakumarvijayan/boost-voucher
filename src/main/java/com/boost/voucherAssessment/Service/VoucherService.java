package com.boost.voucherAssessment.Service;

import com.boost.voucherAssessment.Model.HttpResponse;
import com.boost.voucherAssessment.Model.Recipient;
import com.boost.voucherAssessment.Model.SpecialOffer;
import com.boost.voucherAssessment.Model.VoucherCode;
import com.boost.voucherAssessment.Repository.RecipientRepository;
import com.boost.voucherAssessment.Repository.SpecialOfferRepository;
import com.boost.voucherAssessment.Repository.VoucherCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VoucherService {

    @Autowired
    private VoucherCodeRepository voucherRepo;

    @Autowired
    private RecipientRepository recipientRepo;

    @Autowired
    private SpecialOfferRepository offerRepo;

    // Generate unique voucher codes for all recipients
    public HttpResponse generateVouchers(Long offerId, Date expirationDate) {
        HttpResponse response = new HttpResponse();

        try {
            SpecialOffer offer = offerRepo.findById(offerId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid offer ID"));

            List<Recipient> recipients = recipientRepo.findAll();

            for (Recipient recipient : recipients) {
                String code = generateUniqueCode();
                VoucherCode voucher = new VoucherCode();
                voucher.setCode(code);
                voucher.setRecipient(recipient);
                voucher.setOffer(offer);
                voucher.setExpirationDate(expirationDate);
                voucherRepo.save(voucher);
            }
            response.setMessage("Vouchers generated successfully");
            response.setStatus(HttpStatus.CREATED.value());

            return response;

        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return response;
        }
    }

    // Validate a voucher
    public HttpResponse validateVoucher(String code, String email) {
        HttpResponse response = new HttpResponse();
        try {
            VoucherCode voucher = voucherRepo.findByCode(code)
                    .orElseThrow(() -> new IllegalArgumentException("Voucher not found"));

            if (!voucher.getRecipient().getEmail().equalsIgnoreCase(email)) {
                response.setMessage("Voucher does not belong to this email");
                response.setStatus(HttpStatus.BAD_REQUEST.value());

                return response;
            }

            if (voucher.isUsed()) {
                response.setMessage("Voucher already used");
                response.setStatus(HttpStatus.CONFLICT.value());
                return response;
            }

            if (voucher.isExpired()) {
                response.setMessage("Voucher expired");
                response.setStatus(HttpStatus.GONE.value());
                return response;
            }

            voucher.setDateOfUsage(new Date());
            voucherRepo.save(voucher);
            response.setStatus(HttpStatus.OK.value());
            response.setMessage(String.valueOf(voucher.getOffer().getDiscountPercentage()));

            return response;

        } catch (IllegalArgumentException | IllegalStateException e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return response;
        } catch (Exception e) {
            response.setMessage(e.getMessage());
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return response;
        }
    }

    // List all valid vouchers for a recipient
    public HttpResponse getValidVouchers(String email) {
        HttpResponse response = new HttpResponse();
        try {
            List<VoucherCode> vouchers = voucherRepo.findByRecipientEmailAndDateOfUsageIsNullAndExpirationDateAfter(
                    email, new Date());

            if (vouchers.isEmpty()) {
                response.setStatus(HttpStatus.NO_CONTENT.value());
                response.setMessage("No valid vouchers found");
                return response;
            }

            List<Voucher> validVouchers = vouchers.stream()
                    .map(v -> new Voucher(v.getCode(), v.getOffer().getName()))
                    .collect(Collectors.toList());

            response.setStatus(HttpStatus.OK.value());
            response.setData(validVouchers);
            return response;


        } catch (Exception e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Failed to retrieve valid vouchers: " + e.getMessage());
            return response;

        }
    }

    // Generate a unique random code (8+ characters)
    private String generateUniqueCode() {
        String code;
        do {
            code = UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
        } while (voucherRepo.findByCode(code).isPresent());
        return code;
    }

    // DTO for returning voucher and offer name
    public static class Voucher {
        private String code;
        private String offerName;

        public Voucher(String code, String offerName) {
            this.code = code;
            this.offerName = offerName;
        }

        public String getCode() {
            return code;
        }

        public String getOfferName() {
            return offerName;
        }
    }
}
