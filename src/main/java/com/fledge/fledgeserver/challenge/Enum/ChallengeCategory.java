package com.fledge.fledgeserver.challenge.Enum;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "챌린지 카테고리")
public enum ChallengeCategory {
    LIFE, FINANCIAL_MANAGEMENT, SELF_DEVELOPMENT,
    CERTIFICATION, MIND_CONTROL, EXERCISE
}
