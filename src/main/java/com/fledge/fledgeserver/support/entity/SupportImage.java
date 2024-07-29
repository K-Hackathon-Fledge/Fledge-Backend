package com.fledge.fledgeserver.support.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SupportImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "support_id", nullable = false)
    private Support support;

    @Builder
    public SupportImage(Support support, String imageUrl) {
        this.support = support;
        this.imageUrl = imageUrl;
    }
}
