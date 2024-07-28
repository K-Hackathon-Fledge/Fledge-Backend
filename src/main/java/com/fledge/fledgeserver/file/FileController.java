package com.fledge.fledgeserver.file;

import com.fledge.fledgeserver.file.dto.PresignedUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/presigned-url")
    public ResponseEntity<PresignedUrlResponse> getPresignedUrl(
            @RequestParam(name = "prefix", required = false, defaultValue = "") String prefix,
            @RequestParam(name = "fileName") String fileName) {
        return ResponseEntity.ok(fileService.getPresignedUrl(prefix, fileName));
    }

}
