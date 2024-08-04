package com.fledge.fledgeserver.support.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@Schema(description = "후원하기 게시글 Paging")
public class PostPagingResponse {
    @Schema(description = "", example = "")
    private Long supportId;

    @Schema(description = "", example = "")
    private String title;

    @Schema(description = "", example = "")
    private String leftDays;

    @Schema(description = "", example = "")
    private String supportPostImageUrl;

    @Schema(description = "", example = "")
    private RecordProgressGetResponse supportRecord;



    public PostPagingResponse(Long supportId, String title, LocalDate expirationDate, String supportPostImageUrl, RecordProgressGetResponse supportRecord) {
        this.supportId = supportId;
        this.title = title;
        this.leftDays = String.valueOf(ChronoUnit.DAYS.between(LocalDate.now(), expirationDate));
        this.supportPostImageUrl = supportPostImageUrl;
        this.supportRecord = supportRecord;
    }
}
