package sams.feedbloom.feedback.mapper;

import sams.feedbloom.feedback.dto.ApprovalResponse;
import sams.feedbloom.feedback.entity.Approval;

public class ApprovalMapper {
	public static Approval mapToEntity(ApprovalResponse approvalDto, Approval approval) {
		approval.setFeedback(approvalDto.getFeedback());
		approval.setApprovalStatus(approvalDto.getApprovalStatus());
		approval.setApprovedBy(approvalDto.getApprovedBy());
		approval.setCreatedBy(approvalDto.getCreatedBy());
		return approval;
	}
}
