package com.fledge.fledgeserver.support.repository;

import com.fledge.fledgeserver.support.entity.SupportCategory;
import com.fledge.fledgeserver.support.entity.SupportPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SupportPostRepositoryCustom {
    Page<SupportPost> findByCategoryAndSearchAndSupportPostStatusWithImages(
            List<SupportCategory> category, String q, String status, Pageable pageable);
}

