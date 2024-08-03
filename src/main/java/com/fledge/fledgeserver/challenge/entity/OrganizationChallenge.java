package com.fledge.fledgeserver.challenge.entity;


import com.fledge.fledgeserver.challenge.Enum.ChallengeCategory;
import com.fledge.fledgeserver.challenge.Enum.ChallengeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@DiscriminatorValue("ORGANIZATION")
public class OrganizationChallenge extends Challenge {
    private String supportContent;
    private LocalDate startDate;
    private LocalDate endDate;


    protected OrganizationChallenge() {
        super();
    }

    public OrganizationChallenge(String title, List<ChallengeCategory> categories, String description, int participantCount,
                                 int successCount, ChallengeType type, LocalDate registrationDate, int likeCount,
                                 String supportContent, LocalDate startDate, LocalDate endDate) {
        super(title, categories, description, participantCount, successCount, type, registrationDate, likeCount);
        this.supportContent = supportContent;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}