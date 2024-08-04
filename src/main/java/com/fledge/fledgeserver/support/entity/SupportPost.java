package com.fledge.fledgeserver.support.entity;

import com.fledge.fledgeserver.common.entity.BaseTimeEntity;
import com.fledge.fledgeserver.member.entity.Member;
import com.fledge.fledgeserver.promise.entity.Promise;
import com.fledge.fledgeserver.support.dto.request.PostCreateRequest;
import com.fledge.fledgeserver.support.dto.request.PostUpdateRequest;
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
    public SupportPost(Member member, PostCreateRequest postCreateRequest) {
        this.member = member;
        this.title = postCreateRequest.getTitle();
        this.reason = postCreateRequest.getReason();
        this.item = postCreateRequest.getItem();
        this.purchaseUrl = postCreateRequest.getPurchaseUrl();
        this.price = postCreateRequest.getPrice();
        this.expirationDate = postCreateRequest.getExpirationDate();
        this.promise = Promise.valueOf(postCreateRequest.getPromise());
        this.supportCategory = SupportCategory.valueOf(postCreateRequest.getSupportCategory());

        if ("MEDICAL".equals(supportCategory.name()) || "LEGAL_AID".equals(supportCategory.name())) {
            this.bank = postCreateRequest.getBank();
            this.account = postCreateRequest.getAccount();
            this.recipientName = null;
            this.phone = null;
            this.address = null;
            this.detailAddress = null;
            this.zip = null;
        } else {
            this.recipientName = postCreateRequest.getRecipientName();
            this.phone = postCreateRequest.getPhone();
            this.address = postCreateRequest.getAddress();
            this.detailAddress = postCreateRequest.getDetailAddress();
            this.zip = postCreateRequest.getZip();
            this.bank = null;
            this.account = null;
        }
    }

    public void updateAll(PostUpdateRequest postUpdateRequest) {
        this.supportCategory = SupportCategory.valueOf(postUpdateRequest.getSupportCategory());
        this.title = postUpdateRequest.getTitle();
        this.reason = postUpdateRequest.getReason();
        this.item = postUpdateRequest.getItem();
        this.purchaseUrl = postUpdateRequest.getPurchaseUrl();
        this.price = postUpdateRequest.getPrice();
        this.promise = Promise.valueOf(postUpdateRequest.getPromise());
        this.expirationDate = postUpdateRequest.getExpirationDate();
        this.bank = postUpdateRequest.getBank();
        this.account = postUpdateRequest.getAccount();
        this.recipientName = postUpdateRequest.getRecipientName();
        this.phone = postUpdateRequest.getPhone();
        this.address = postUpdateRequest.getAddress();
        this.detailAddress = postUpdateRequest.getDetailAddress();
        this.zip = postUpdateRequest.getZip();
    }

    public void updateNotPending(PostUpdateRequest postUpdateRequest) {
        this.bank = postUpdateRequest.getBank();
        this.account = postUpdateRequest.getAccount();
        this.recipientName = postUpdateRequest.getRecipientName();
        this.phone = postUpdateRequest.getPhone();
        this.address = postUpdateRequest.getAddress();
        this.detailAddress = postUpdateRequest.getDetailAddress();
        this.zip = postUpdateRequest.getZip();
    }



    public void support() {
        this.supportPostStatus = SupportPostStatus.IN_PROGRESS;
    }
}

