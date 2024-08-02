package com.fledge.fledgeserver.promise.entity;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Promise {
    ONCE("ONCE"), // 1회 인증
    WEEKLY("WEEKLY"), // 4주간 매 주 인증
    MONTHLY("MONTHLY"); // 3개월간 매 달 인증

    private final String key;
    public String getKey() { return key; }
}