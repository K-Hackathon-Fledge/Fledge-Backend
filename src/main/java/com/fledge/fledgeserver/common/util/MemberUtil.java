package com.fledge.fledgeserver.common.util;

import com.fledge.fledgeserver.exception.CustomException;
import com.fledge.fledgeserver.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

import java.security.Principal;

@RequiredArgsConstructor
public class MemberUtil {
    /**
     * 현재 사용자의 OAuth2 ID를 반환합니다.
     */
    public static Long getMemberId(Principal principal) {
        // Principal 객체가 null이면 사용자가 인증되지 않았으므로 예외를 발생시킨다.
        if (principal == null) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }
        // Principal 객체의 이름을 memberId ID로 사용하여 반환한다.
        return Long.valueOf(principal.getName());
    }
}


