package sams.feedbloom.feedback.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import sams.feedbloom.authentication.entity.User;
import sams.feedbloom.common.entity.Common;

@Getter
@Setter
@Entity
@Table(name = "approval", indexes = {
		@Index(name = "idx_approval_feedback_id", columnList = "feedback_id"),
		@Index(name = "idx_approval_approved_by", columnList = "approved_by")
})
public class Approval extends Common {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "feedback_id", nullable = false)
	private Feedback feedback;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by", nullable = false)
	private User approvedBy;
	
	@Column(nullable = false)
	private Boolean approvalStatus = false;
}
