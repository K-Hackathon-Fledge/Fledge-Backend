package com.fledge.fledgeserver.scheduler;

import com.fledge.fledgeserver.challenge.service.ChallengeParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChallengeCompletionScheduler {

    private final ChallengeParticipationService participationService;

    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    public void checkChallengeCompletion() {
        System.out.println("===START ChallengeCompletionScheduler===");
        participationService.checkMissedProofs();
        participationService.checkAndMarkChallengeSuccess();
    }
}

