package com.fledge.fledgeserver.common.Interface;

import com.fledge.fledgeserver.file.FileService;

public interface PresignedUrlApplicable {
    void applyPresignedUrls(FileService fileService);
}
