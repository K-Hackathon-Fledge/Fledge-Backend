package com.fledge.fledgeserver.exception.dto;

import com.fledge.fledgeserver.exception.ErrorCode;
import com.fledge.fledgeserver.response.ApiResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@Builder
@ToString
public class ErrorResponse {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String code;
    private final String missValue;

//    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode, String value) {
//        return ResponseEntity
//                .status(errorCode.getHttpStatus())
//                .body(ErrorResponse.builder()
//                        .status(errorCode.getHttpStatus().value())
//                        .error(errorCode.getHttpStatus().name())
//                        .code(errorCode.name())
//                        .missValue(value)
//                        .build()
//                );
//    }

    public static <T> ResponseEntity<ApiResponse<T>> toResponseEntity(ErrorCode errorCode, String message) {
        return ApiResponse.fail(errorCode.getHttpStatus().value(), message);
    }
}
