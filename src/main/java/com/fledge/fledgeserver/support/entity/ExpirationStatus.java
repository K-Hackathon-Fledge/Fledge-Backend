package com.fledge.fledgeserver.support.entity;

public enum ExpirationStatus {
    ACTIVE("ACTIVE"),

    EXPIRED("EXPIRED");
    private final String key;

    ExpirationStatus(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}