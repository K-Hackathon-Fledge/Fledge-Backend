package com.fledge.fledgeserver.canary.controller;

import com.fledge.fledgeserver.canary.dto.CanaryProfileRequest;
import com.fledge.fledgeserver.canary.dto.CanaryProfileResponse;
import com.fledge.fledgeserver.canary.dto.CanaryProfileUpdateRequest;
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

    @GetMapping("/{userId}")
    public ResponseEntity<CanaryProfileResponse> getCanaryProfile(@PathVariable Long userId) {
        CanaryProfileResponse response = canaryProfileService.getCanaryProfile(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateCanaryProfile(@PathVariable Long userId, @Valid @RequestBody CanaryProfileUpdateRequest request) {
        canaryProfileService.updateCanaryProfile(userId, request);
        return ResponseEntity.ok().build();
    }
}
