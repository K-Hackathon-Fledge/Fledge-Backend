package com.fledge.fledgeserver.member.entity;

public enum Role {
    USER("ROLE_USER"),
    MENTOR("ROLE_MENTOR"),
    CANARY("ROLE_CANARY");

    private final String key;

    Role(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
