package com.fledge.fledgeserver.canary.dto;

import com.fledge.fledgeserver.canary.entity.CanaryProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Date;

@Getter
@Schema(description = "자립준비청년 프로필 조회 응답 DTO")
public class CanaryProfileResponse {

    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phone;

    @Schema(description = "생년월일", example = "1990-01-01")
    private Date birth;

    @Schema(description = "성별", example = "true")
    private Boolean gender;

    @Schema(description = "자기 소개", example = "안녕하세요, 저는...")
    private String introduction;

    @Schema(description = "거주지", example = "서울특별시 강남구 역삼동")
    private String address;

    @Schema(description = "상세 주소", example = "123-45")
    private String detailAddress;

    @Schema(description = "우편번호", example = "12345")
    private String zip;

    @Schema(description = "위도", example = "37.5665")
    private Double latitude;

    @Schema(description = "경도", example = "126.9780")
    private Double longitude;

    @Schema(description = "관심 지역", example = "서울특별시 전체, 서울특별시 서초구")
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
