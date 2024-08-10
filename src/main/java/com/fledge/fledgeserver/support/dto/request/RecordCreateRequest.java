package com.fledge.fledgeserver.support.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "후원 기록 생성 DTO")
public class RecordCreateRequest {

    @Schema(description = "후원 계좌 은행 이름", required = true, example = "국민은행")
    @NotBlank(message = "은행 이름은 필수입니다.")
    private String bankName;

    @Schema(description = "후원 계좌 은행 코드", required = true, example = "004")
    @NotBlank(message = "은행 코드는 필수입니다.")
    private String bankCode;

    @Schema(description = "후원 계좌", required = true, example = "123-456-789012")
    @NotBlank(message = "계좌는 필수입니다.")
    private String account;

    @Schema(description = "후원 금액", required = true, example = "50000")
    @NotNull(message = "후원 금액은 필수입니다.")
    @Positive(message = "후원 금액은 0보다 큰 숫자여야 합니다.")
    private int amount;
}