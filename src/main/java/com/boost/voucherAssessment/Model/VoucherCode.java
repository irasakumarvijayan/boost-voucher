package com.boost.voucherAssessment.Model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
public class VoucherCode {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String code;

    @ManyToOne
    private Recipient recipient;

    @ManyToOne
    private SpecialOffer offer;

    private Date expirationDate;
    private Date dateOfUsage;

    public boolean isUsed() {
        return dateOfUsage != null;
    }

    public boolean isExpired() {
        return expirationDate.before(new Date());
    }

    // getters/setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }

    public SpecialOffer getOffer() {
        return offer;
    }

    public void setOffer(SpecialOffer offer) {
        this.offer = offer;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Date getDateOfUsage() {
        return dateOfUsage;
    }

    public void setDateOfUsage(Date dateOfUsage) {
        this.dateOfUsage = dateOfUsage;
    }

    @Override
    public String toString() {
        return "VoucherCode{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", recipient=" + recipient +
                ", offer=" + offer +
                ", expirationDate=" + expirationDate +
                ", dateOfUsage=" + dateOfUsage +
                '}';
    }
}