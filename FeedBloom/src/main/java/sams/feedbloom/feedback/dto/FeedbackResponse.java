package sams.feedbloom.feedback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import sams.feedbloom.common.dto.CommonDTO;
import sams.feedbloom.feedback.entity.FeedbackCategory;
import sams.feedbloom.feedback.entity.FeedbackPriority;
import sams.feedbloom.feedback.entity.FeedbackStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FeedbackResponse extends CommonDTO {
	private Long id;
	private String title;
	private String description;
	private Long feedbackBy;
	private Long projectId;
	private FeedbackStatus status;
	private FeedbackCategory category;
	private FeedbackPriority priority;
}
