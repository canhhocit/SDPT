package com.phananh.e_commerce.usermanagement.domain.model;

import com.phananh.e_commerce.core.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;

@Embeddable
public record UserInfo(
        @Column(nullable = false)
        String fullName,
        @Column(unique = true, length = 100)
        String email,
        @Column(columnDefinition = "TEXT")
        String address,
        @Column(unique = true, nullable = false)
        String phoneNumber) {

    @Builder
    public UserInfo(String fullName, String email, String address, String phoneNumber) {
        if (StringUtils.isBlank(fullName)) {
            throw new IllegalArgumentException("Full name cannot be null or blank");
        }
        if (StringUtils.isBlank(phoneNumber)) {
            throw new IllegalArgumentException("Phone number cannot be null or blank");
        }

        this.fullName = fullName.trim();
        this.email = StringUtils.isBlank(email) ? null : email.trim();
        this.address = address.trim();
        this.phoneNumber = StringUtils.isBlank(phoneNumber) ? null : phoneNumber.trim();
    }
}
