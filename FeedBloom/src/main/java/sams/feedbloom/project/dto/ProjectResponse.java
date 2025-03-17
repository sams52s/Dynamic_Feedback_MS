package sams.feedbloom.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sams.feedbloom.common.dto.CommonDTO;
import sams.feedbloom.feedback.dto.FeedbackResponse;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponse extends CommonDTO {
	private Long id;
	private String name;
	private String createdBy;
	private List<FeedbackResponse> feedbackList;
}
