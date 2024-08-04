package com.fledge.fledgeserver.challenge.Enum;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "챌린지 타입")
public enum ChallengeType {
    GENERAL, PARTNERSHIP, ORGANIZATION
}
