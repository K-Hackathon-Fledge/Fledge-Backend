package com.fledge.fledgeserver.support.entity;

import com.fledge.fledgeserver.common.entity.BaseTimeEntity;
import com.fledge.fledgeserver.member.entity.Member;
import com.fledge.fledgeserver.promise.entity.Promise;
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
    private LocalDate expirationDate;

    @Column(nullable = false)
    private Boolean expirationStatus = false;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Promise promise; // 인증 주기 및 횟수

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private SupportCategory supportCategory;

    // ------의료비 또는 법률구조비------
    @Column(nullable = true)
    private String bank;

    @Column(nullable = true)
    private String account;

    // -----------기타-----------
    @Column(nullable = true)
    private String recipientName;

    @Column(nullable = true)
    private String phone;

    @Column(nullable = true)
    private String address;

    @Column(nullable = true)
    private String detailAddress;

    @Column(nullable = true)
    private String zip;

    // TODO :: 챌린지 구현 후 참여 중이거나 완료한 챌린지(뱃지)에 대한 로직 추가

    @Builder
    public Support(Member member, SupportCreateRequestDto supportCreateRequestDto) {
        this.member = member;
        this.title = supportCreateRequestDto.getTitle();
        this.reason = supportCreateRequestDto.getReason();
        this.item = supportCreateRequestDto.getItem();
        this.purchaseUrl = supportCreateRequestDto.getPurchaseUrl();
        this.price = supportCreateRequestDto.getPrice();
        this.expirationDate = supportCreateRequestDto.getExpirationDate();
        this.promise = Promise.valueOf(supportCreateRequestDto.getPromise());
        this.supportCategory = SupportCategory.valueOf(supportCreateRequestDto.getSupportCategory());

        if ("MEDICAL".equals(supportCategory.name()) || "LEGAL_AID".equals(supportCategory.name())) {
            this.bank = supportCreateRequestDto.getBank();
            this.account = supportCreateRequestDto.getAccount();
            this.recipientName = null;
            this.phone = null;
            this.address = null;
            this.detailAddress = null;
            this.zip = null;
        } else {
            this.recipientName = supportCreateRequestDto.getRecipientName();
            this.phone = supportCreateRequestDto.getPhone();
            this.address = supportCreateRequestDto.getAddress();
            this.detailAddress = supportCreateRequestDto.getDetailAddress();
            this.zip = supportCreateRequestDto.getZip();
            this.bank = null;
            this.account = null;
        }
    }



    public void update(SupportUpdateRequestDto supportUpdateRequestDto) {
        this.title = supportUpdateRequestDto.getTitle();
        this.reason = supportUpdateRequestDto.getReason();
        this.item = supportUpdateRequestDto.getItem();
        this.purchaseUrl = supportUpdateRequestDto.getPurchaseUrl();
        this.price = supportUpdateRequestDto.getPrice();
        this.recipientName = supportUpdateRequestDto.getRecipientName();
        this.phone = supportUpdateRequestDto.getPhone();
        this.address = supportUpdateRequestDto.getAddress();
        this.detailAddress = supportUpdateRequestDto.getDetailAddress();
        this.zip = supportUpdateRequestDto.getZip();
        this.expirationDate = supportUpdateRequestDto.getExpirationDate();
    }

}

