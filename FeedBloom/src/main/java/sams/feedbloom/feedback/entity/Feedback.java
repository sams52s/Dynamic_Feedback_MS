package sams.feedbloom.feedback.entity;

import jakarta.persistence.*;
import lombok.Data;
import sams.feedbloom.authentication.entity.User;
import sams.feedbloom.common.entity.Common;
import sams.feedbloom.project.entity.Project;

@Data
@Entity
@Table(name = "feedback", indexes = {
		@Index(name = "idx_feedback_user_id", columnList = "user_id"),
		@Index(name = "idx_feedback_project_id", columnList = "project_id")
})
public class Feedback extends Common {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User feedbackBy;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", nullable = false)
	private Project project;
	
	@Column(nullable = false, length = 255)
	private String title;
	
	@Column(nullable = false, columnDefinition = "TEXT")
	private String description;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 50)
	private FeedbackStatus status = FeedbackStatus.PENDING;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 50)
	private FeedbackCategory category = FeedbackCategory.IMPROVEMENT;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 50)
	private FeedbackPriority priority = FeedbackPriority.LOW;
}

