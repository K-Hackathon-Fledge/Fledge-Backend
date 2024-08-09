package com.fledge.fledgeserver.support.repository;

import com.fledge.fledgeserver.exception.ErrorCode;
import com.fledge.fledgeserver.support.entity.SupportImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SupportImageRepository extends JpaRepository<SupportImage, Long> {
    @Query("SELECT si FROM SupportImage si WHERE si.supportPost.id = :supportPostId ORDER BY si.id ASC")
    List<SupportImage> findImagesBySupportPostId(@Param("supportPostId") Long supportPostId);

    default SupportImage findFirstImageBySupportPostIdOrDefault(Long supportPostId) {
        List<SupportImage> images = findImagesBySupportPostId(supportPostId);
        return images.isEmpty() ? null : images.get(0);
    }

    @Modifying
    @Query("UPDATE SupportImage si SET si.deletedAt = :deletedAt WHERE si.id IN (:ids)")
    void softDeleteByIds(@Param("ids") List<Long> ids, @Param("deletedAt") LocalDateTime deletedAt);
}
