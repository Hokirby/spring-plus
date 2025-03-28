package org.example.expert.domain.user.enums;

import lombok.Getter;
import org.example.expert.domain.common.exception.InvalidRequestException;

import java.util.Arrays;

public enum UserRole {
    ROLE_USER(Authority.USER),
    ROLE_ADMIN(Authority.ADMIN);

    @Getter
    private final String authority;

    UserRole(String authority) {
        this.authority = authority;
    }

    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new InvalidRequestException("유효하지 않은 UserRole"));
    }
    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}
