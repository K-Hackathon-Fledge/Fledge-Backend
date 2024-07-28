package com.fledge.fledgeserver.canary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CanaryProfileRequest {

    @NotNull(message = "User ID는 필수입니다.")
    private Long userId;

    @NotBlank(message = "전화번호는 필수입니다.")
    @Size(max = 255, message = "전화번호는 최대 255자까지 입력 가능합니다.")
    private String phone;

    @NotNull(message = "생년월일은 필수입니다.")
    @Past(message = "생년월일은 과거 날짜여야 합니다.")
    private Date birth;

    @NotNull(message = "성별은 필수입니다.")
    private Boolean gender;

    @NotBlank(message = "거주지는 필수입니다.")
    @Size(max = 255, message = "거주지는 최대 255자까지 입력 가능합니다.")
    private String address;

    @NotBlank(message = "상세 주소는 필수입니다.")
    @Size(max = 255, message = "상세 주소는 최대 255자까지 입력 가능합니다.")
    private String detailAddress;

    @NotBlank(message = "우편번호는 필수입니다.")
    @Size(max = 255, message = "우편번호는 최대 255자까지 입력 가능합니다.")
    private String zip;

    @NotBlank(message = "보호종료확인서 경로는 필수입니다.")
    private String certificateFilePath;

    @NotNull(message = "위도는 필수입니다.")
    private Double latitude;

    @NotNull(message = "경도는 필수입니다.")
    private Double longitude;
}
