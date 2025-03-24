package sams.feedbloom.comment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import sams.feedbloom.common.entity.CommonEntity;
import sams.feedbloom.feedback.entity.Feedback;
import sams.feedbloom.user.entity.User;

@Getter
@Setter
@Entity
@Table(name = "comment", indexes = {
		@Index(name = "idx_comment_user_id", columnList = "user_id"),
		@Index(name = "idx_comment_feedback_id", columnList = "feedback_id")
})
public class Comment extends CommonEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "feedback_id", nullable = false)
	private Feedback feedback;
	
	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;
}
