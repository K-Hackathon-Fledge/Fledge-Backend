package com.fledge.fledgeserver.challenge.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class TopParticipantResponse {
    private Long memberId;
    private Long participationCount;
    private Long successCount;
    private Double successRate;
    private List<String> topCategories;
}
