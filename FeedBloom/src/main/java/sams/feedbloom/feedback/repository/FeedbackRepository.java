package sams.feedbloom.feedback.repository;

import org.springframework.stereotype.Repository;
import sams.feedbloom.common.repository.CommonRepository;
import sams.feedbloom.feedback.entity.Feedback;

import java.util.LinkedList;

@Repository
public interface FeedbackRepository extends CommonRepository<Feedback, Long> {
	LinkedList<Feedback> findByIsDeletedAndCreatedByOrderByCreatedAtAsc(Boolean isDeleted, String createdBy);
	
	Feedback findByIsDeletedFalseAndId(Long id);
}
