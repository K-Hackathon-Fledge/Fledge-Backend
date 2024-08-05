package com.fledge.fledgeserver.support.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@Schema(description = "후원하기 게시글 Paging")
public class PostPagingResponse {
    @Schema(description = "후원하기 게시글 ID", example = "2")
    private Long supportId;

    @Schema(description = "후원하기 게시글 제목", example = "")
    private String title;

    @Schema(description = "남은 기간", example = "99")
    private String leftDays;

    @Schema(description = "후원하기 게시글 사진 URL", example = "https://fledge-bucket.s3.ap-northeast-2.amazonaws.com/asdfndsvaksmdf")
    private String supportPostImageUrl;

    @Schema(description = "후원 받은 내역", example = "")
    private RecordProgressGetResponse supportRecord;



    public PostPagingResponse(Long supportId, String title, LocalDate expirationDate, String supportPostImageUrl, RecordProgressGetResponse supportRecord) {
        this.supportId = supportId;
        this.title = title;
        this.leftDays = String.valueOf(ChronoUnit.DAYS.between(LocalDate.now(), expirationDate));
        this.supportPostImageUrl = supportPostImageUrl;
        this.supportRecord = supportRecord;
    }
}
