package com.fledge.fledgeserver.support.entity;

import com.fledge.fledgeserver.common.entity.BaseTimeEntity;
import com.fledge.fledgeserver.member.entity.Member;
import com.fledge.fledgeserver.support.dto.request.SupportCreateRequestDto;
import com.fledge.fledgeserver.support.dto.request.SupportUpdateRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Support extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") //댓글 작성자 id
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false)
    private String item;

    @Column(nullable = false)
    private String purchaseUrl;

    @Column(nullable = false)
    private int price;

    @OneToMany(mappedBy = "support", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SupportImage> images = new ArrayList<>();

    @Column(nullable = false)
    private int checkPeriod;

    @Column(nullable = false)
    private int checkCount;

    @Column(nullable = false)
    private String recipientName;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String detailAddress;

    @Column(nullable = false)
    private String zip;

    @Column(nullable = false)
    private LocalDate expirationDate;

    @Column(nullable = false)
    private Boolean expirationStatus = false;

    // TODO :: 챌린지 구현 후 참여 중이거나 완료한 챌린지(뱃지)에 대한 로직 추가

    @Builder
    public Support(Member member, SupportCreateRequestDto supportCreateRequestDto) {
        this.member = member;
        this.title = supportCreateRequestDto.getTitle();
        this.reason = supportCreateRequestDto.getReason();
        this.item = supportCreateRequestDto.getItem();
        this.purchaseUrl = supportCreateRequestDto.getPurchaseUrl();
        this.price = supportCreateRequestDto.getPrice();
        this.checkPeriod = supportCreateRequestDto.getCheckPeriod();
        this.checkCount = supportCreateRequestDto.getCheckCount();
        this.recipientName = supportCreateRequestDto.getRecipientName();
        this.phone = supportCreateRequestDto.getPhone();
        this.address = supportCreateRequestDto.getAddress();
        this.detailAddress = supportCreateRequestDto.getDetailAddress();
        this.zip = supportCreateRequestDto.getZip();
        this.expirationDate = supportCreateRequestDto.getExpirationDate();
    }

    public void update(SupportUpdateRequestDto supportUpdateRequestDto) {
        this.title = supportUpdateRequestDto.getTitle();
        this.reason = supportUpdateRequestDto.getReason();
        this.item = supportUpdateRequestDto.getItem();
        this.purchaseUrl = supportUpdateRequestDto.getPurchaseUrl();
        this.price = supportUpdateRequestDto.getPrice();
        this.checkPeriod = supportUpdateRequestDto.getCheckPeriod();
        this.checkCount = supportUpdateRequestDto.getCheckCount();
        this.recipientName = supportUpdateRequestDto.getRecipientName();
        this.phone = supportUpdateRequestDto.getPhone();
        this.address = supportUpdateRequestDto.getAddress();
        this.detailAddress = supportUpdateRequestDto.getDetailAddress();
        this.zip = supportUpdateRequestDto.getZip();
        this.expirationDate = supportUpdateRequestDto.getExpirationDate();
    }

}

