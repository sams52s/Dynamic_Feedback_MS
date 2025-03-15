package sams.feedbloom.feedback.repository;

import org.springframework.stereotype.Repository;
import sams.feedbloom.common.repository.CommonRepository;
import sams.feedbloom.feedback.entity.Feedback;

@Repository
public interface FeedbackRepository extends CommonRepository<Feedback, Long> {
}
