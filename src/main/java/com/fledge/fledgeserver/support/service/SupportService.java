package com.fledge.fledgeserver.support.service;

import com.fledge.fledgeserver.support.dto.request.SupportCreateRequestDto;
import com.fledge.fledgeserver.support.entity.Support;
import com.fledge.fledgeserver.support.entity.SupportImage;
import com.fledge.fledgeserver.support.repository.SupportImageRepository;
import com.fledge.fledgeserver.support.repository.SupportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SupportService {
    private final SupportRepository supportRepository;
    private final SupportImageRepository supportImageRepository;

    public Long createSupport(SupportCreateRequestDto supportCreateRequestDto) {

        Support support = Support.builder()
                .title(supportCreateRequestDto.getTitle())
                .reason(supportCreateRequestDto.getReason())
                .item((supportCreateRequestDto.getItem()))
                .price(supportCreateRequestDto.getPrice())
                .purchaseUrl(supportCreateRequestDto.getPurchaseUrl())
                .checkPeriod(supportCreateRequestDto.getCheckPeriod())
                .checkCount(supportCreateRequestDto.getCheckCount())
                .expirationTime(supportCreateRequestDto.getExpirationTime())
                .build();

        for (String imageUrl : supportCreateRequestDto.getImages()) {
            SupportImage supportImage = SupportImage.builder()
                    .support(support)
                    .imageUrl(imageUrl)
                    .build();
            support.getImages().add(supportImage);
        }

        return supportRepository.save(support).getId();
    }
}
