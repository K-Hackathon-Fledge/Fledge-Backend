package com.fledge.fledgeserver.support.entity;

import com.fledge.fledgeserver.common.entity.BaseTimeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Support extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private LocalDate expirationDate;

    @Column(nullable = false)
    private ExpirationStatus expirationStatus = ExpirationStatus.ACTIVE;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String detailAddress;

    @Column(nullable = false)
    private String zip;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    // TODO :: 챌린지 구현 후 참여 중이거나 완료한 챌린지(뱃지)에 대한 로직 추가

    @Builder
    public Support(String title, String reason, String item, String purchaseUrl, int price, List<SupportImage> images, int checkPeriod, int checkCount, LocalDate expirationDate, String address, String detailAddress, String zip, String name, String phone) {
        this.title = title;
        this.reason = reason;
        this.item = item;
        this.purchaseUrl = purchaseUrl;
        this.price = price;
        this.images = images;
        this.checkPeriod = checkPeriod;
        this.checkCount = checkCount;
        this.expirationDate = expirationDate;
        this.address = address;
        this.detailAddress = detailAddress;
        this.zip = zip;
        this.name = name;
        this.phone = phone;
    }
}

