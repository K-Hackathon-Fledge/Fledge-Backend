package com.fledge.fledgeserver.challenge;

import com.fledge.fledgeserver.challenge.Enum.ChallengeCategory;
import com.fledge.fledgeserver.challenge.Enum.ChallengeType;
import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @ElementCollection(targetClass = ChallengeCategory.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "challenge_categories", joinColumns = @JoinColumn(name = "challenge_id"))
    @Column(name = "category")
    private List<ChallengeCategory> categories;

    private String description;

    private int participantCount;

    private int successCount;

    @Enumerated(EnumType.STRING)
    private ChallengeType type;

    private LocalDate registrationDate;

    private int likeCount;

}
