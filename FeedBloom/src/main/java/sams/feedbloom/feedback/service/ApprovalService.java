package sams.feedbloom.feedback.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sams.feedbloom.feedback.dto.ApprovalResponse;
import sams.feedbloom.feedback.entity.Approval;
import sams.feedbloom.feedback.entity.Feedback;
import sams.feedbloom.feedback.mapper.ApprovalMapper;
import sams.feedbloom.feedback.repository.ApprovalRepository;
import sams.feedbloom.feedback.repository.FeedbackRepository;

@Service
@RequiredArgsConstructor
public class ApprovalService {
	
	private final ApprovalRepository approvalRepository;
	private final FeedbackRepository feedbackRepository;
	
	public void update(ApprovalResponse approvalDto) {
		approvalDto.setFeedback(getFeedbackById(approvalDto.getFeedbackId()));
		approvalRepository.save(ApprovalMapper.mapToEntity(approvalDto, new Approval()));
	}
	
	public Feedback getFeedbackById(Long id) {
		return feedbackRepository.findById(id)
		                         .orElseThrow(() -> new EntityNotFoundException("Feedback not found with ID: " + id));
	}
}