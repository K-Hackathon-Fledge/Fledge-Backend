package com.fledge.fledgeserver.support.entity;

import com.fledge.fledgeserver.common.entity.BaseTimeEntity;
import com.fledge.fledgeserver.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SupportRecord extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "support_post_id", nullable = false)
    private SupportPost supportPost;

    @Column(nullable = false)
    private String bankName;

    @Column(nullable = false)
    private String bankCode;

    @Column(nullable = false)
    private String account;

    @Column(nullable = false)
    private int amount;

    @Column(name = "deleted_at") // 삭제 시각 저장
    private LocalDateTime deletedAt;

    @Builder
    public SupportRecord(Member member, SupportPost supportPost, String account, int amount, String bankName, String bankCode) {
        this.member = member;
        this.supportPost = supportPost;
        this.bankName = bankName;
        this.bankCode = bankCode;
        this.account = account;
        this.amount = amount;
    }
}
