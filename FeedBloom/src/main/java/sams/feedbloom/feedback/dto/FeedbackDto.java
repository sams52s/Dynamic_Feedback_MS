package sams.feedbloom.feedback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import sams.feedbloom.common.dto.CommonDTO;
import sams.feedbloom.feedback.entity.FeedbackCategory;
import sams.feedbloom.feedback.entity.FeedbackPriority;
import sams.feedbloom.feedback.entity.FeedbackStatus;
import sams.feedbloom.project.entity.Project;
import sams.feedbloom.user.entity.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FeedbackDto extends CommonDTO {
	private Long id;
	private String title;
	private String description;
	private User feedbackBy;
	private Long projectId;
	private String projectName;
	private Project project;
	private FeedbackStatus status;
	private FeedbackCategory category;
	private FeedbackPriority priority;
}
