package sams.feedbloom.feedback.repository;

import sams.feedbloom.common.repository.CommonRepository;
import sams.feedbloom.feedback.entity.FeedbackHistory;

import java.util.LinkedList;

public interface FeedbackHistoryRepository extends CommonRepository<FeedbackHistory, Long> {
	LinkedList<FeedbackHistory> findByIsDeletedFalseAndFeedbackIdOrderByCreatedAtAsc(Long feedbackId);
}