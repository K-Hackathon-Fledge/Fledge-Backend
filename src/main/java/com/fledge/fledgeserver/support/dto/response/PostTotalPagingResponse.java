package com.fledge.fledgeserver.support.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "후원하기 게시글 Paging - Total 페이지 수 포함")
public class PostTotalPagingResponse {
    private int totalPages;
    private List<PostPagingResponse> supportPosts;
    public PostTotalPagingResponse(int totalPages, List<PostPagingResponse> supportPosts) {
        this.totalPages = totalPages;
        this.supportPosts = supportPosts;
    }
}
