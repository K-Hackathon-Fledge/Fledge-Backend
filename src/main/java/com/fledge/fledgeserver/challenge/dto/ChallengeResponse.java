package com.fledge.fledgeserver.challenge.dto;

import com.fledge.fledgeserver.challenge.Enum.ChallengeCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ChallengeResponse {
    private final String title;
    private final int likeCount;
    private final List<ChallengeCategory> categories;
    private final String description;
    private final double successRate;
    private final int participantCount;
    private String supportContent;
    private LocalDate startDate;
    private LocalDate endDate;
}

