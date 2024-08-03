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
@DiscriminatorValue("GENERAL")
public class GeneralChallenge extends Challenge {
    public GeneralChallenge() {
        super();
    }

    public GeneralChallenge(String title, List<ChallengeCategory> categories, String description, int participantCount,
                            int successCount, ChallengeType type, LocalDate registrationDate, int likeCount) {
        super(title, categories, description, participantCount, successCount, type, registrationDate, likeCount);
    }
}