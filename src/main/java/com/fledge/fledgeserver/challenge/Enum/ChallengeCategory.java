package com.fledge.fledgeserver.challenge.Enum;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "챌린지 카테고리")
public enum ChallengeCategory {
    LIFE, RESIDENCE, FINANCIAL_MANAGEMENT, EMPLOYMENT, LEARNING, SELF_DEVELOPMENT,
    WELLBEING, CERTIFICATION, MIND_CONTROL, NETWORKING, TIME_MANAGEMENT
}
