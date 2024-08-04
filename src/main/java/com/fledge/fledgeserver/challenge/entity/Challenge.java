package com.fledge.fledgeserver.challenge.entity;

import com.fledge.fledgeserver.challenge.Enum.ChallengeCategory;
import com.fledge.fledgeserver.challenge.Enum.ChallengeType;
import com.fledge.fledgeserver.challenge.Enum.Frequency;
import lombok.*;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
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
    @Column(insertable = false, updatable = false)
    private ChallengeType type;

    private LocalDate registrationDate;

    private int likeCount;

    private int periodWeeks;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Frequency frequency;

    public Challenge(String title, List<ChallengeCategory> categories, String description, int participantCount,
                     int successCount, ChallengeType type, LocalDate registrationDate, int likeCount, int periodWeeks, Frequency frequency) {
        this.title = title;
        this.categories = categories;
        this.description = description;
        this.participantCount = participantCount;
        this.successCount = successCount;
        this.type = type;
        this.registrationDate = registrationDate;
        this.likeCount = likeCount;
        this.periodWeeks = periodWeeks;
        this.frequency = frequency;
    }
}
