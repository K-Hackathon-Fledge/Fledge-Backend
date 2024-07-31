package com.fledge.fledgeserver.common.utils;

import com.fledge.fledgeserver.auth.dto.OAuthUserImpl;
import com.fledge.fledgeserver.exception.CustomException;
import com.fledge.fledgeserver.member.entity.Member;
import org.springframework.security.core.Authentication;

import java.security.Principal;

import static com.fledge.fledgeserver.exception.ErrorCode.MEMBER_NOT_FOUND;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    // TODO : 인증객체 가져오는 방식 통일한 뒤 사용하지 않는 메소드 정리

    public static OAuthUserImpl getCurrentUser(Principal principal) {
        if (principal == null) {
            throw new CustomException(MEMBER_NOT_FOUND);
        }

        if (principal instanceof Authentication) {
            Authentication authentication = (Authentication) principal;
            Object userPrincipal = authentication.getPrincipal();

            if (userPrincipal instanceof OAuthUserImpl) {
                return (OAuthUserImpl) userPrincipal;
            }
        }

        throw new CustomException(MEMBER_NOT_FOUND);
    }

    public static OAuthUserImpl getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomException(MEMBER_NOT_FOUND);
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof OAuthUserImpl) {
            return (OAuthUserImpl) principal;
        } else {
            throw new CustomException(MEMBER_NOT_FOUND);
        }
    }

    public static Long getCurrentUserId(Principal principal) {
        return Long.valueOf(getCurrentUser(principal).getUsername());
    }

    public static Long getCurrentUserId() {
        return Long.valueOf(getCurrentUser().getUsername());
    }

    public static Member getCurrentMember(OAuthUserImpl oAuthUser) {
        if (oAuthUser == null) {
            throw new CustomException(MEMBER_NOT_FOUND);
        }
        return oAuthUser.getMember();
    }

    public static Member getCurrentMember() {
        if (getCurrentUser() == null) {
            throw new CustomException(MEMBER_NOT_FOUND);
        }
        return getCurrentUser().getMember();
    }

}
