package com.fledge.fledgeserver.challenge.dto;

import com.fledge.fledgeserver.challenge.Enum.ChallengeCategory;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ChallengeResponse {
    private String title;
    private int likeCount;
    private List<ChallengeCategory> categories;
    private String description;
    private double successRate;
    private int participantCount;
}
