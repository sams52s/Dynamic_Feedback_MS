package sams.feedbloom.feedback.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sams.feedbloom.feedback.dto.FeedbackAttachmentResponse;
import sams.feedbloom.feedback.entity.FeedbackAttachment;
import sams.feedbloom.feedback.repository.FeedbackAttachmentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackAttachmentService {
	
	private final FeedbackAttachmentRepository feedbackAttachmentRepository;
	
	public FeedbackAttachmentResponse getById(Long id) {
		FeedbackAttachment attachment = feedbackAttachmentRepository.findById(id)
		                                                            .orElseThrow(() -> new RuntimeException("Attachment not found with id: " + id));
		return mapToResponse(attachment);
	}
	
	public List<FeedbackAttachmentResponse> getAll() {
		return feedbackAttachmentRepository.findAll().stream()
		                                   .map(this::mapToResponse)
		                                   .collect(Collectors.toList());
	}
	
	public void create(FeedbackAttachmentResponse attachmentResponse) {
		FeedbackAttachment attachment = new FeedbackAttachment();
		attachment.setAttachmentUrl(attachmentResponse.getAttachmentUrl());
		feedbackAttachmentRepository.save(attachment);
	}
	
	public void delete(Long id) {
		feedbackAttachmentRepository.deleteById(id);
	}
	
	private FeedbackAttachmentResponse mapToResponse(FeedbackAttachment attachment) {
		FeedbackAttachmentResponse response = new FeedbackAttachmentResponse();
		response.setId(attachment.getId());
		response.setFeedbackId(attachment.getFeedback().getId());
		response.setAttachmentUrl(attachment.getAttachmentUrl());
		return response;
	}
}