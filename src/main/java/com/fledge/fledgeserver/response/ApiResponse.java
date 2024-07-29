package com.fledge.fledgeserver.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.http.ResponseEntity;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    @Schema(description = "HTTP 상태 코드", example = "200")
    private final int status;

    @Schema(description = "성공 여부", example = "true")
    private final boolean success;

    @Schema(description = "응답 메시지", example = "요청이 성공적으로 처리되었습니다.")
    private final String message;

    @Schema(description = "응답 데이터")
    private T data;

    public static <T> ResponseEntity<ApiResponse<T>> success(SuccessStatus successStatus) {
        return ResponseEntity.status(successStatus.getHttpStatus())
                .body(ApiResponse.<T>builder()
                        .status(successStatus.getStatusCode())
                        .success(true)
                        .message(successStatus.getMessage()).build());
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(SuccessStatus successStatus, T data) {
        return ResponseEntity.status(successStatus.getHttpStatus())
                .body(ApiResponse.<T>builder()
                        .status(successStatus.getStatusCode())
                        .success(true)
                        .message(successStatus.getMessage())
                        .data(data).build());
    }

    public static <T> ResponseEntity<ApiResponse<T>> fail(int status, String message) {
        return ResponseEntity.status(status)
                .body(ApiResponse.<T>builder()
                        .status(status)
                        .success(false)
                        .message(message).build());
    }
}