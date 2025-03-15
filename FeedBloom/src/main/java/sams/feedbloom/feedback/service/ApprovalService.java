package sams.feedbloom.feedback.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sams.feedbloom.feedback.dto.ApprovalResponse;
import sams.feedbloom.feedback.entity.Approval;
import sams.feedbloom.feedback.repository.ApprovalRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApprovalService {
	
	private final ApprovalRepository approvalRepository;
	
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
		response.setApprovedBy(approval.getApprovedBy().getId());
		response.setApprovalStatus(approval.getApprovalStatus());
		return response;
	}
}