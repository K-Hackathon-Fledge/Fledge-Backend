package com.fledge.fledgeserver.support.repository;

import com.fledge.fledgeserver.exception.ErrorCode;
import com.fledge.fledgeserver.support.entity.SupportImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.webjars.NotFoundException;

import java.util.Optional;

public interface SupportImageRepository extends JpaRepository<SupportImage, Long> {
    @Query("SELECT si FROM SupportImage si WHERE si.supportPost.id = :supportPostId ORDER BY si.id ASC")
    Optional<SupportImage> findFirstImageBySupportPostId(@Param("supportPostId") Long supportPostId);

    default SupportImage findFirstImageBySupportPostIdOrDefault(Long supportPostId) {
        return findFirstImageBySupportPostId(supportPostId).orElse(null);
    }
}
