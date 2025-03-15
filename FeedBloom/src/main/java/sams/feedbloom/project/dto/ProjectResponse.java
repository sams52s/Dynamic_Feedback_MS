package sams.feedbloom.project.dto;

import lombok.Data;
import sams.feedbloom.feedback.dto.FeedbackResponse;

import java.util.List;

@Data
public class ProjectResponse {
	private Long id;
	private String name;
	private String createdBy;
	private List<FeedbackResponse> feedbackList;
}
