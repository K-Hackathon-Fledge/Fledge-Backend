package com.fledge.fledgeserver.support.entity;

import com.fledge.fledgeserver.common.entity.BaseTimeEntity;
import com.fledge.fledgeserver.member.entity.Member;
import com.fledge.fledgeserver.promise.entity.Promise;
import com.fledge.fledgeserver.support.dto.request.SupportPostCreateRequest;
import com.fledge.fledgeserver.support.dto.request.SupportPostUpdateRequest;
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
public class SupportPost extends BaseTimeEntity {

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

    @OneToMany(mappedBy = "supportPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SupportImage> images = new ArrayList<>();

    @Column(nullable = false)
    private LocalDate expirationDate;

    @Column(nullable = false)
    private Boolean expirationStatus = false;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Promise promise;

    @OneToMany(mappedBy = "supportPost", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SupportRecord> supportRecords = new ArrayList<>();

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private SupportPostStatus supportPostStatus = SupportPostStatus.PENDING;

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
    public SupportPost(Member member, SupportPostCreateRequest supportPostCreateRequest) {
        this.member = member;
        this.title = supportPostCreateRequest.getTitle();
        this.reason = supportPostCreateRequest.getReason();
        this.item = supportPostCreateRequest.getItem();
        this.purchaseUrl = supportPostCreateRequest.getPurchaseUrl();
        this.price = supportPostCreateRequest.getPrice();
        this.expirationDate = supportPostCreateRequest.getExpirationDate();
        this.promise = Promise.valueOf(supportPostCreateRequest.getPromise());
        this.supportCategory = SupportCategory.valueOf(supportPostCreateRequest.getSupportCategory());

        if ("MEDICAL".equals(supportCategory.name()) || "LEGAL_AID".equals(supportCategory.name())) {
            this.bank = supportPostCreateRequest.getBank();
            this.account = supportPostCreateRequest.getAccount();
            this.recipientName = null;
            this.phone = null;
            this.address = null;
            this.detailAddress = null;
            this.zip = null;
        } else {
            this.recipientName = supportPostCreateRequest.getRecipientName();
            this.phone = supportPostCreateRequest.getPhone();
            this.address = supportPostCreateRequest.getAddress();
            this.detailAddress = supportPostCreateRequest.getDetailAddress();
            this.zip = supportPostCreateRequest.getZip();
            this.bank = null;
            this.account = null;
        }
    }

    public void updateAll(SupportPostUpdateRequest supportPostUpdateRequest) {
        this.supportCategory = SupportCategory.valueOf(supportPostUpdateRequest.getSupportCategory());
        this.title = supportPostUpdateRequest.getTitle();
        this.reason = supportPostUpdateRequest.getReason();
        this.item = supportPostUpdateRequest.getItem();
        this.purchaseUrl = supportPostUpdateRequest.getPurchaseUrl();
        this.price = supportPostUpdateRequest.getPrice();
        this.promise = Promise.valueOf(supportPostUpdateRequest.getPromise());
        this.expirationDate = supportPostUpdateRequest.getExpirationDate();
        this.bank = supportPostUpdateRequest.getBank();
        this.account = supportPostUpdateRequest.getAccount();
        this.recipientName = supportPostUpdateRequest.getRecipientName();
        this.phone = supportPostUpdateRequest.getPhone();
        this.address = supportPostUpdateRequest.getAddress();
        this.detailAddress = supportPostUpdateRequest.getDetailAddress();
        this.zip = supportPostUpdateRequest.getZip();
    }

    public void updateNotPending(SupportPostUpdateRequest supportPostUpdateRequest) {
        this.bank = supportPostUpdateRequest.getBank();
        this.account = supportPostUpdateRequest.getAccount();
        this.recipientName = supportPostUpdateRequest.getRecipientName();
        this.phone = supportPostUpdateRequest.getPhone();
        this.address = supportPostUpdateRequest.getAddress();
        this.detailAddress = supportPostUpdateRequest.getDetailAddress();
        this.zip = supportPostUpdateRequest.getZip();
    }



    public void support() {
        this.supportPostStatus = SupportPostStatus.IN_PROGRESS;
    }
}

