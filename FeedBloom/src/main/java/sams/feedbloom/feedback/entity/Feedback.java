package sams.feedbloom.feedback.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import sams.feedbloom.common.entity.CommonEntity;
import sams.feedbloom.project.entity.Project;
import sams.feedbloom.user.entity.User;

@Data
@Entity
@ToString(exclude = "project")
@Table(name = "feedback", indexes = {
		@Index(name = "idx_feedback_user_id", columnList = "user_id"),
		@Index(name = "idx_feedback_project_id", columnList = "project_id")
})
public class Feedback extends CommonEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User feedbackBy;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "project_id", nullable = false)
	@JsonIgnore
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
