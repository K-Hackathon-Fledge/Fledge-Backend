package com.fledge.fledgeserver.support.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "후원하기 게시글 Paging - Total 페이지 수 포함")
public class PostTotalPagingResponse {
    @Schema(description = "총 게시글 수", example = "100")
    private int totalPosts;

    @Schema(description = "총 페이지 수", example = "10")
    private int totalPages;

    @Schema(description = "후원하기 게시글 목록")
    private List<PostPagingResponse> supportPosts;

    public PostTotalPagingResponse(int totalPosts, int totalPages, List<PostPagingResponse> supportPosts) {
        this.totalPosts = totalPosts;
        this.totalPages = totalPages;
        this.supportPosts = supportPosts;
    }
}
