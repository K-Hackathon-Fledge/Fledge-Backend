package com.fledge.fledgeserver.file.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PresignedUrlResponse {
    private final String url;
    private final String filePath;
}