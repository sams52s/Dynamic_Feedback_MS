package sams.feedbloom.feedback.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import sams.feedbloom.common.entity.Common;

@Getter
@Setter
@Entity
@Table(name = "feedback_attachment", indexes = {
		@Index(name = "idx_feedback_attachment_feedback_id", columnList = "feedback_id")
})
public class FeedbackAttachment extends Common {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "feedback_id", nullable = false)
	private Feedback feedback;
	
	@Column(nullable = false, columnDefinition = "TEXT")
	private String attachmentUrl;
}
