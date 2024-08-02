package com.fledge.fledgeserver.canary.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
@Schema(description = "자립준비청년 배송지 정보 조회 응답 DTO")
public class CanaryGetDeliveryInfoResponse {

    @Schema(description = "실명", example = "홍길동")
    private String name;

    @Schema(description = "거주지", example = "서울특별시 강남구 역삼동")
    private String address;

    @Schema(description = "상세 주소", example = "123-45")
    private String detailAddress;

    @Schema(description = "우편번호", example = "12345")
    private String zip;

    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phone;


    public CanaryGetDeliveryInfoResponse(String name, String address, String detailAddress, String zip,  String phone) {
        this.name = name;
        this.address = address;
        this.detailAddress = detailAddress;
        this.zip = zip;
        this.phone = phone;
    }
}
