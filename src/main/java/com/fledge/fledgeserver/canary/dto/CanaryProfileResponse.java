package com.fledge.fledgeserver.canary.dto;

import com.fledge.fledgeserver.canary.entity.CanaryProfile;
import lombok.Getter;

import java.util.Date;

@Getter
public class CanaryProfileResponse {

    private Long id;
    private String phone;
    private Date birth;
    private Boolean gender;
    private String introduction;
    private String address;
    private String detailAddress;
    private String zip;
    private Double latitude;
    private Double longitude;
    private String interestArea;

    public CanaryProfileResponse(CanaryProfile canaryProfile) {
        this.id = canaryProfile.getId();
        this.phone = canaryProfile.getPhone();
        this.birth = canaryProfile.getBirth();
        this.gender = canaryProfile.getGender();
        this.introduction = canaryProfile.getIntroduction();
        this.address = canaryProfile.getAddress();
        this.detailAddress = canaryProfile.getDetailAddress();
        this.zip = canaryProfile.getZip();
        this.latitude = canaryProfile.getLatitude();
        this.longitude = canaryProfile.getLongitude();
        this.interestArea = canaryProfile.getInterestArea();
    }
}
