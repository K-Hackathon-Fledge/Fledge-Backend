package com.fledge.fledgeserver.file;

import com.fledge.fledgeserver.auth.dto.OAuthUserImpl;
import com.fledge.fledgeserver.file.dto.PresignedUrlResponse;
import com.fledge.fledgeserver.member.dto.MemberResponse;
import com.fledge.fledgeserver.response.ApiResponse;
import com.fledge.fledgeserver.response.SuccessStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Tag(name = "파일 관리 API", description = "파일 업로드 및 다운로드 관련 API")
@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @Operation(summary = "프리사인드 URL 생성", description = "파일 업로드를 위한 프리사인드 URL을 생성합니다. 유효기간 15분 입니다.")
    @GetMapping("/presigned-url")
    public ResponseEntity<ApiResponse<PresignedUrlResponse>> getPresignedUrl(
            @Parameter(description = "파일 경로의 prefix", example = "images") @RequestParam(name = "prefix", required = false, defaultValue = "") String prefix,
            @Parameter(description = "파일 이름", required = true, example = "example.txt") @RequestParam(name = "fileName") String fileName) {
        return ApiResponse.success(SuccessStatus.FILE_RETRIEVAL_SUCCESS,  fileService.getPresignedUrl(prefix, fileName));
    }

}
