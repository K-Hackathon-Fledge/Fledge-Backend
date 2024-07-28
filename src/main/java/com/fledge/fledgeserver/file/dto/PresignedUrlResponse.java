package com.fledge.fledgeserver.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "프리사인드 URL 응답 DTO")
public class PresignedUrlResponse {

    @Schema(description = "생성된 프리사인드 URL", example = "https://example.com/presigned-url")
    private final String url;

    @Schema(description = "파일 경로", example = "images/example.txt")
    private final String filePath;
}