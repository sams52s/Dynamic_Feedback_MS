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
import sams.feedbloom.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApprovalService {
	
	private final ApprovalRepository approvalRepository;
	private final FeedbackRepository feedbackRepository;
	private final UserRepository userRepository;
	
	public ApprovalResponse getById(Long id) {
		Approval approval = approvalRepository.findById(id)
		                                      .orElseThrow(() -> new RuntimeException("Approval not found with id: " + id));
		return mapToResponse(approval);
	}
	
	public List<ApprovalResponse> getAll() {
		return approvalRepository.findAll().stream()
		                         .map(this::mapToResponse)
		                         .collect(Collectors.toList());
	}
	
	public void update(ApprovalResponse approvalDto) {
		approvalDto.setFeedback(getFeedbackById(approvalDto.getFeedbackId()));
		approvalRepository.save(ApprovalMapper.mapToEntity(approvalDto, new Approval()));
	}
	
	public Feedback getFeedbackById(Long id) {
		return feedbackRepository.findById(id)
		                         .orElseThrow(() -> new EntityNotFoundException("Feedback not found with ID: " + id));
	}
	
	public void approve(Long id) {
		Approval approval = approvalRepository.findById(id)
		                                      .orElseThrow(() -> new RuntimeException("Approval not found with id: " + id));
		approval.setApprovalStatus(true);
		approvalRepository.save(approval);
	}
	
	private ApprovalResponse mapToResponse(Approval approval) {
		ApprovalResponse response = new ApprovalResponse();
		response.setId(approval.getId());
		response.setFeedbackId(approval.getFeedback().getId());
		response.setApprovedById(approval.getApprovedBy().getId());
		response.setApprovedBy(approval.getApprovedBy());
		response.setFeedback(approval.getFeedback());
		response.setApprovalStatus(approval.getApprovalStatus());
		return response;
	}
}