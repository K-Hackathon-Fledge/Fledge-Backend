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
    @JoinColumn(name = "support_post_id", nullable = false)
    private SupportPost supportPost;
    

    @Builder
    public SupportImage(SupportPost supportPost, String imageUrl) {
        this.supportPost = supportPost;
        this.imageUrl = imageUrl;
    }
}
