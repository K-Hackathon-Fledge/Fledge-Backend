package com.fledge.fledgeserver.support.entity;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SupportPostStatus {
    PENDING("PENDING"), // 후원 대기 중 (수정 가능)
    IN_PROGRESS("IN_PROGRESS"), // 후원 진행 중 (최소 정보만 수정 가능)
    COMPLETED("COMPLETED"), // 후원 금액 달성으로 종료
    TERMINATED("TERMINATED"); // 후원 기간 말료로 종료

    private final String key;
    public String getKey() {
        return key;
    }
}
