package com.fledge.fledgeserver.support.entity;

import com.fledge.fledgeserver.common.entity.BaseTimeEntity;
import com.fledge.fledgeserver.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SupportRecord extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Member와의 다대일 관계
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY) // Support와의 다대일 관계
    @JoinColumn(name = "support_id", nullable = false)
    private Support support;

    @Column(nullable = false)
    private String account;

    @Builder
    public SupportRecord(Member member, Support support, String account) {
        this.member = member;
        this.support = support;
        this.account = account;
    }
}
