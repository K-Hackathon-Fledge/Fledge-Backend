package com.fledge.fledgeserver.challenge.Enum;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "챌린지 빈도")
public enum Frequency {
    DAILY, ONE_WEEK, TWO_WEEKS, FOUR_WEEKS
}
