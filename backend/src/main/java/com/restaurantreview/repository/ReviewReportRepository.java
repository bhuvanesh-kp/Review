package com.restaurantreview.repository;

import com.restaurantreview.constants.ReportStatus;
import com.restaurantreview.entities.ReviewReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewReportRepository extends JpaRepository<ReviewReport, Long> {

	Page<ReviewReport> findByStatus(ReportStatus status, Pageable pageable);

	boolean existsByReviewIdAndReporterId(Long reviewId, Long reporterId);

}
