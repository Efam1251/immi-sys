package com.immi.system.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum RoleEnum {
    USER,
    ADMIN;

    @JsonCreator
    public static RoleEnum fromString(String value) {
        if (value != null) {
            for (RoleEnum role : RoleEnum.values()) {
                if (role.name().equalsIgnoreCase(value)) {
                    return role;
                }
            }
        }
        throw new IllegalArgumentException("Unknown role: " + value);
    }
}
