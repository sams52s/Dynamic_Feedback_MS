package sams.feedbloom.feedback.mapper;

import sams.feedbloom.authentication.entity.User;
import sams.feedbloom.feedback.dto.FeedbackResponse;
import sams.feedbloom.feedback.entity.Feedback;
import sams.feedbloom.project.entity.Project;

public class FeedbackMapper {
	public static Feedback mapToEntity(FeedbackResponse feedbackResponse) {
		Feedback feedback = new Feedback();
		feedback.setTitle(feedbackResponse.getTitle());
		feedback.setDescription(feedbackResponse.getDescription());
		
		User user = new User();
		user.setId(feedbackResponse.getFeedbackBy());
		feedback.setFeedbackBy(user);
		
		Project project = new Project();
		project.setId(feedbackResponse.getProjectId());
		feedback.setProject(project);
		
		feedback.setStatus(feedbackResponse.getStatus());
		feedback.setCategory(feedbackResponse.getCategory());
		feedback.setPriority(feedbackResponse.getPriority());
		
		return feedback;
	}
	
	public static FeedbackResponse mapToResponse(Feedback feedback) {
		return new FeedbackResponse() {
			{
				setId(feedback.getId());
				setTitle(feedback.getTitle());
				setDescription(feedback.getDescription());
				setFeedbackBy(feedback.getFeedbackBy().getId());
				setProjectId(feedback.getProject().getId());
				setStatus(feedback.getStatus());
				setCategory(feedback.getCategory());
				setPriority(feedback.getPriority());
				setCreatedAt(feedback.getCreatedAt());
			}
		};
	}
}
