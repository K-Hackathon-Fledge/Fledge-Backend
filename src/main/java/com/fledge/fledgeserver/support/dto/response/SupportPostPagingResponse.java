package com.fledge.fledgeserver.support.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@Schema(description = "후원하기 게시글 Paging")
public class SupportPostPagingResponse {
    @Schema(description = "", example = "")
    private Long supportId;

    @Schema(description = "", example = "")
    private String title;

    @Schema(description = "", example = "")
    private String leftDays;

    @Schema(description = "", example = "")
    private SupportRecordProgressGetResponse supportRecord;

    public SupportPostPagingResponse(Long supportId, String title, LocalDate expirationDate, SupportRecordProgressGetResponse supportRecord) {
        this.supportId = supportId;
        this.title = title;
        this.leftDays = String.valueOf(ChronoUnit.DAYS.between(LocalDate.now(), expirationDate));
        this.supportRecord = supportRecord;
    }
}
