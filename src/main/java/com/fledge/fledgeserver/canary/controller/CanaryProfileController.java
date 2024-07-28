package com.fledge.fledgeserver.canary.controller;

import com.fledge.fledgeserver.canary.dto.CanaryProfileRequest;
import com.fledge.fledgeserver.canary.service.CanaryProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/canary")
public class CanaryProfileController {

    private final CanaryProfileService canaryProfileService;

    @PostMapping("/apply")
    public ResponseEntity<Void> applyForCanaryProfile(@Valid @RequestBody CanaryProfileRequest request) {
        canaryProfileService.createCanaryProfile(request);
        return ResponseEntity.ok().build();
    }
}
