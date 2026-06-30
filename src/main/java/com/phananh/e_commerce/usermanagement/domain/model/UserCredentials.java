package com.phananh.e_commerce.usermanagement.domain.model;

import com.phananh.e_commerce.core.exception.AppException;
import com.phananh.e_commerce.core.exception.ErrorCode;
import com.phananh.e_commerce.core.util.PasswordUtils;
import com.phananh.e_commerce.core.util.StringUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record UserCredentials(
        @Column(nullable = false, unique = true, length = 50)
        String username,

        @Column(nullable = false)
        String password,

        @Column(nullable = false)
        Boolean isEnabled) {

    /** Dùng khi tạo mới — encode password */
    public static UserCredentials of(String username, String rawPassword, Boolean isEnabled) {
        if (StringUtils.isBlank(username)) throw new IllegalArgumentException("Username cannot be null or blank");
        if (StringUtils.isBlank(rawPassword)) throw new IllegalArgumentException("Password cannot be null or blank");
        if (isEnabled == null) throw new IllegalArgumentException("Enabled cannot be null");
        return new UserCredentials(username.trim(), PasswordUtils.encode(rawPassword.trim()), isEnabled);
    }

    public UserCredentials changePassword(String oldPassword, String newPassword) {
        if (!PasswordUtils.matches(oldPassword, this.password)) {
            throw new AppException(ErrorCode.OLD_PASSWORD_INCORRECT);
        }
        return UserCredentials.of(this.username, newPassword, this.isEnabled);
    }

    public UserCredentials activeUser() {
        return new UserCredentials(this.username, this.password, true);
    }

    public UserCredentials inactiveUser() {
        return new UserCredentials(this.username, this.password, false);
    }
}
