package com.fledge.fledgeserver.support.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Schema(description = "후원하기 게시글 생성 DTO")
public class SupportCreateRequestDto {

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

    @Schema(description = "만료 시점", required = true, example = "2024-12-31T23:59:59")
    @NotBlank(message = "만료 시점은 필수입니다.")
    @Future(message = "만료 시점은 현재 시간 이후여야 합니다.")
    private LocalDateTime expirationTime;
}
