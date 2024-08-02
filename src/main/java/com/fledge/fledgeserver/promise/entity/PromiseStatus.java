package com.fledge.fledgeserver.promise.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PromiseStatus {
    PENDING("PENDING"), // 인증 대기중
    VERIFIED("VERIFIED"), // 인증 완료
    UNVERIFIED("UNVERIFIED"); // 인증하지 않음


    private final String key;

    public String getStatus() {
        return key;
    }
}
