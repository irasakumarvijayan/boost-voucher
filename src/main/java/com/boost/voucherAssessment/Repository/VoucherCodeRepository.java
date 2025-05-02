package com.boost.voucherAssessment.Repository;

import com.boost.voucherAssessment.Model.VoucherCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherCodeRepository extends JpaRepository<VoucherCode, Long> {
    Optional<VoucherCode> findByCode(String code);

    List<VoucherCode> findByRecipientEmailAndDateOfUsageIsNullAndExpirationDateAfter(String email, Date now);
}