package com.fledge.fledgeserver.canary.entity;

import com.fledge.fledgeserver.canary.dto.CanaryProfileUpdateRequest;
import com.fledge.fledgeserver.common.entity.BaseTimeEntity;
import com.fledge.fledgeserver.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class CanaryProfile extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date birth;

    @Column(nullable = false)
    private Boolean gender;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String detailAddress;

    @Column(nullable = false)
    private String zip;

    @Column(columnDefinition = "TEXT")
    private String interestArea;

    @Column(nullable = false)
    private String certificateFilePath;

    @Column(nullable = false)
    private Boolean approvalStatus;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = false)
    private Double longitude;

    @Builder
    public CanaryProfile(Member member, String phone, Date birth, Boolean gender, String address, String detailAddress,
                         String zip, String certificateFilePath, String interestArea, Boolean approvalStatus,
                         Double latitude, Double longitude) {
        this.member = member;
        this.phone = phone;
        this.birth = birth;
        this.gender = gender;
        this.introduction = introduction;
        this.address = address;
        this.detailAddress = detailAddress;
        this.zip = zip;
        this.certificateFilePath = certificateFilePath;
        this.interestArea = interestArea;
        this.approvalStatus = approvalStatus;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void update(CanaryProfileUpdateRequest request) {
        this.phone = request.getPhone();
        this.birth = request.getBirth();
        this.gender = request.getGender();
        this.introduction = request.getIntroduction();
        this.address = request.getAddress();
        this.detailAddress = request.getDetailAddress();
        this.zip = request.getZip();
        this.interestArea = request.getInterestArea();
        this.latitude = request.getLatitude();
        this.longitude = request.getLongitude();
    }
}
