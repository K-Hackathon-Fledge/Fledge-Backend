package com.fledge.fledgeserver.scheduler;

import com.fledge.fledgeserver.challenge.service.ChallengeParticipationService;
import com.fledge.fledgeserver.support.service.SupportService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SupportExpirationScheduler {
    private final SupportService supportService;

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 만료된 게시글 처리
    public void checkSupportPostExpiration() {
        System.out.println("===START SupportPostExpirationScheduler===");
        supportService.checkAndExpireSupportPosts();
    }
}