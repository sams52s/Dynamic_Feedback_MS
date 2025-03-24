package sams.feedbloom.feedback.mapper;

import sams.feedbloom.feedback.dto.FeedbackDto;
import sams.feedbloom.feedback.entity.Feedback;

public class FeedbackMapper {
	public static Feedback mapToEntity(FeedbackDto feedbackDto, Feedback feedback) {
		
		feedback.setTitle(feedbackDto.getTitle());
		
		feedback.setDescription(feedbackDto.getDescription());
		feedback.setFeedbackBy(feedbackDto.getFeedbackBy());
		feedback.setProject(feedbackDto.getProject());
		feedback.setStatus(feedbackDto.getStatus());
		feedback.setCategory(feedbackDto.getCategory());
		feedback.setPriority(feedbackDto.getPriority());
		
		feedback.setCreatedAt(feedbackDto.getCreatedAt());
		feedback.setCreatedBy(feedbackDto.getCreatedBy());
		feedback.setUpdatedAt(feedbackDto.getUpdatedAt());
		feedback.setUpdatedBy(feedbackDto.getUpdatedBy());
		
		return feedback;
	}
	
	public static FeedbackDto mapToResponse(Feedback feedback) {
		FeedbackDto dto = new FeedbackDto();
		dto.setId(feedback.getId());
		dto.setTitle(feedback.getTitle());
		dto.setDescription(feedback.getDescription());
		dto.setCategory(feedback.getCategory());
		dto.setPriority(feedback.getPriority());
		dto.setStatus(feedback.getStatus());
		dto.setFeedbackBy(feedback.getFeedbackBy());
		dto.setProjectId(feedback.getProject().getId());
		dto.setProjectName(feedback.getProject().getName());
		dto.setCreatedAt(feedback.getCreatedAt());
		return dto;
	}
	
}
