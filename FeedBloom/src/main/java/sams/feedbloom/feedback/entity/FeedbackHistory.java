package sams.feedbloom.feedback.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import sams.feedbloom.authentication.entity.User;
import sams.feedbloom.common.entity.Common;

@Getter
@Setter
@Entity
@Table(name = "feedback_history", indexes = {
		@Index(name = "idx_feedback_history_changed_by", columnList = "changed_by"),
		@Index(name = "idx_feedback_history_feedback_id", columnList = "feedback_id")
})
public class FeedbackHistory extends Common {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "feedback_id", nullable = false)
	private Feedback feedback;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "changed_by", nullable = false)
	private User changedBy;
	
	@Column(nullable = false, columnDefinition = "TEXT")
	private String changeDescription;
}
