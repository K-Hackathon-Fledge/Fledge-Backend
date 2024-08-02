package com.fledge.fledgeserver.canary.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@Schema(description = "자립준비청년 프로필 수정 요청 DTO")
public class CanaryProfileUpdateRequest {

    @Schema(description = "실명", required = true, example = "홍길동")
    @NotBlank(message = "실명은 필수입니다.")
    @Size(max = 10, message = "실명은 최대 10자까지 입력 가능합니다.")
    private String name;

    @Schema(description = "전화번호", required = true, example = "010-1234-5678")
    @NotBlank(message = "전화번호는 필수입니다.")
    @Size(max = 20, message = "전화번호는 최대 20자까지 입력 가능합니다.")
    private String phone;

    @Schema(description = "생년월일", required = true, example = "1990-01-01")
    @NotNull(message = "생년월일은 필수입니다.")
    @Past(message = "생년월일은 과거 날짜여야 합니다.")
    private Date birth;

    @Schema(description = "성별", required = true, example = "true")
    @NotNull(message = "성별은 필수입니다.")
    private Boolean gender;

    @Schema(description = "자기 소개", example = "안녕하세요, 저는...")
    private String introduction;

    @Schema(description = "거주지", required = true, example = "서울특별시 강남구 역삼동")
    @NotBlank(message = "거주지는 필수입니다.")
    @Size(max = 255, message = "거주지는 최대 255자까지 입력 가능합니다.")
    private String address;

    @Schema(description = "상세 주소", required = true, example = "123-45")
    @NotBlank(message = "상세 주소는 필수입니다.")
    @Size(max = 255, message = "상세 주소는 최대 255자까지 입력 가능합니다.")
    private String detailAddress;

    @Schema(description = "우편번호", required = true, example = "12345")
    @NotBlank(message = "우편번호는 필수입니다.")
    @Size(max = 20, message = "우편번호는 최대 20자까지 입력 가능합니다.")
    private String zip;

    @Schema(description = "위도", required = true, example = "37.5665")
    @NotNull(message = "위도는 필수입니다.")
    private Double latitude;

    @Schema(description = "경도", required = true, example = "126.9780")
    @NotNull(message = "경도는 필수입니다.")
    private Double longitude;

    @Schema(description = "관심 지역", example = "서울특별시 전체, 서울특별시 서초구")
    private String interestArea;
}