package com.fledge.fledgeserver.support.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Schema(description = "후원하기 게시글 수정 DTO")
public class SupportUpdateRequestDto {

    @Schema(description = "후원 게시글 제목", required = true, example = "후원 요청")
    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 100, message = "제목은 최대 100자까지 입력 가능합니다.")
    private String title;

    @Schema(description = "후원 필요한 이유", required = true, example = "자립을 위한 후원")
    @NotBlank(message = "후원 사유는 필수입니다.")
    @Size(max = 500, message = "후원 사유는 최대 500자까지 입력 가능합니다.")
    private String reason;

    @Schema(description = "후원 물품 명", required = true, example = "노트북")
    @NotBlank(message = "후원 물품 명은 필수입니다.")
    @Size(max = 100, message = "후원 물품 명은 최대 100자까지 입력 가능합니다.")
    private String item;

    @Schema(description = "구매 URL", required = true, example = "https://example.com/product/1")
    @NotBlank(message = "구매 URL은 필수입니다.")
    @URL(message = "유효한 URL 형식이어야 합니다.")
    private String purchaseUrl;

    @Schema(description = "후원 물품 가격", required = true, example = "500000")
    @NotBlank(message = "후원 물품 가격은 필수입니다.")
    @Positive(message = "가격은 0보다 큰 값이어야 합니다.")
    private int price;

    @Schema(description = "후원 물품 이미지 리스트", required = true)
    private List<String> images;

    @Schema(description = "후원 인증 기간", required = true, example = "30")
    @NotBlank(message = "후원 인증 기간은 필수입니다.")
    @Positive(message = "후원 인증 기간은 0보다 큰 값이어야 합니다.")
    private int checkPeriod;

    @Schema(description = "후원 인증 횟수", required = true, example = "1")
    @NotBlank(message = "후원 인증 횟수는 필수입니다.")
    @Positive(message = "후원 인증 횟수는 0보다 큰 값이어야 합니다.")
    private int checkCount;

    @Schema(description = "만료 시점", required = true, example = "2024-12-31")
    @NotBlank(message = "만료 시점은 필수입니다.")
    @Future(message = "만료 시점은 현재 시간 이후여야 합니다.")
    private LocalDate expirationDate;

    @Schema(description = "수령인 이름", required = true, example = "홍길동")
    @NotBlank(message = "수령인 이름은 필수입니다.")
    private String recipientName;

    @Schema(description = "전화번호", required = true, example = "010-1234-5678")
    @NotBlank(message = "전화번호는 필수입니다.")
    private String phone;

    @Schema(description = "주소", required = true, example = "서울특별시 노원구 공릉로232")
    @NotBlank(message = "주소는 필수입니다.")
    private String address;

    @Schema(description = "상세 주소", required = true, example = "OO빌라 101호")
    @NotBlank(message = "상세 주소는 필수입니다.")
    private String detailAddress;

    @Schema(description = "우편번호", required = true, example = "123456")
    @NotBlank(message = "우편번호는 필수입니다.")
    private String zip;
}
