package com.fledge.fledgeserver.challenge.dto.response;

import com.fledge.fledgeserver.challenge.entity.ChallengeProof;
import com.fledge.fledgeserver.common.Interface.PresignedUrlApplicable;
import com.fledge.fledgeserver.file.FileService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;


@AllArgsConstructor
@Getter
@Schema(description = "챌린지 인증 응답 DTO")
public class ChallengeProofResponse implements PresignedUrlApplicable {

    @Schema(description = "챌린지 인증 ID", example = "1")
    private Long id;

    @Schema(description = "참여 ID", example = "1")
    private Long participationId;

    @Schema(description = "인증 날짜", example = "2023-01-02")
    private LocalDate proofDate;

    @Schema(description = "인증 이미지 URL", example = "http://example.com/proof.jpg")
    private String proofImageUrl;

    @Schema(description = "인증 여부", example = "true")
    private boolean proofed;


    public ChallengeProofResponse(ChallengeProof proof) {
        this.id = proof.getId();
        this.participationId = proof.getParticipation().getId();
        this.proofDate = proof.getProofDate();
        this.proofImageUrl = proof.getProofImageUrl();
        this.proofed = proof.isProofed();
    }

    @Override
    public void applyPresignedUrls(FileService fileService) {
        if (this.proofImageUrl != null) {
            this.proofImageUrl = fileService.getDownloadPresignedUrl(this.proofImageUrl);
        }
    }

}
