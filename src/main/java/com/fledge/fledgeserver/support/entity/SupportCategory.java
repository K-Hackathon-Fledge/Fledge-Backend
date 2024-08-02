package com.fledge.fledgeserver.support.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SupportCategory {
    DAILY_NECESSITY("DAILY_NECESSITY"), // 생필품
    FOOD("FOOD"), // 식품
    HOME_APPLIANCES("HOME_APPLIANCES"), // 가전제품
    EDUCATION("EDUCATION"), // 교육비/교재비
    MEDICAL("MEDICAL"), // 의료비
    LEGAL_AID("LEGAL_AID"), // 법률 구조비
    ETC("ETC"); // 기타

    private final String key;
    public String getKey() {
        return key;
    }
}