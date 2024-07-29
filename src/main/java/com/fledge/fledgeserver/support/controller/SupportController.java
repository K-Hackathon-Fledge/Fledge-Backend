package com.fledge.fledgeserver.support.controller;

import com.fledge.fledgeserver.support.dto.request.SupportCreateRequestDto;
import com.fledge.fledgeserver.support.service.SupportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@Tag(name = "후원하기 API", description = "후원하기와 관련된 API")
@RestController
@RequestMapping("/api/v1/supports")
@RequiredArgsConstructor
public class SupportController {
    private final SupportService supportService;

    @Operation(summary = "후원하기 게시글 등록", description = "후원하기 게시글을 등록합니다.(자립 준비 청소년만)")
    @PostMapping
    public ResponseEntity<URI> createSupport(@RequestBody SupportCreateRequestDto supportCreateRequestDto){
        Long createdId = supportService.createSupport(supportCreateRequestDto);
        URI location = URI.create(String.format("/api/supports/%d", createdId));
        return ResponseEntity.created(location).body(location);
    }


}
