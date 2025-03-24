package sams.feedbloom.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sams.feedbloom.comment.entity.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findByFeedbackIdOrderByCreatedAtDescCreatedAt(Long feedbackId);
}
