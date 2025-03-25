package sams.feedbloom.feedback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import sams.feedbloom.common.dto.CommonDTO;
import sams.feedbloom.feedback.entity.Feedback;
import sams.feedbloom.user.entity.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FeedbackHistoryResponse extends CommonDTO {
	private Long id;
	private Long feedbackId;
	private Feedback feedback;
	private User changedBy;
	private String changeDescription;
	
}