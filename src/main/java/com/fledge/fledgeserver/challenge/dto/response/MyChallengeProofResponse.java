package com.fledge.fledgeserver.challenge.dto.response;

import com.fledge.fledgeserver.common.Interface.PresignedUrlApplicable;
import com.fledge.fledgeserver.file.FileService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "본인 챌린지 인증 내역 응답 DTO")
public class MyChallengeProofResponse implements PresignedUrlApplicable {

    @Schema(description = "필요한 인증 총 횟수", example = "7")
    private int totalProofs;

    @Schema(description = "현재 인증 내역 리스트")
    private List<ProofDetailResponse> proofDetailResponses;

    @Override
    public void applyPresignedUrls(FileService fileService) {
        if (this.proofDetailResponses != null) {
            for (ProofDetailResponse proofDetail : proofDetailResponses) {
                proofDetail.applyPresignedUrls(fileService);
            }
        }
    }
}
