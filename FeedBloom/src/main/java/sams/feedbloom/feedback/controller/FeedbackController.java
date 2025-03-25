package sams.feedbloom.feedback.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sams.feedbloom.authentication.service.AuthService;
import sams.feedbloom.comment.service.CommentService;
import sams.feedbloom.feedback.dto.ApprovalResponse;
import sams.feedbloom.feedback.dto.FeedbackDto;
import sams.feedbloom.feedback.dto.FeedbackHistoryResponse;
import sams.feedbloom.feedback.entity.Feedback;
import sams.feedbloom.feedback.entity.FeedbackCategory;
import sams.feedbloom.feedback.entity.FeedbackPriority;
import sams.feedbloom.feedback.entity.FeedbackStatus;
import sams.feedbloom.feedback.service.ApprovalService;
import sams.feedbloom.feedback.service.FeedbackHistoryService;
import sams.feedbloom.feedback.service.FeedbackService;
import sams.feedbloom.project.service.ProjectService;
import sams.feedbloom.user.dto.UserDTO;
import sams.feedbloom.user.entity.User;
import sams.feedbloom.user.entity.UserRole;
import sams.feedbloom.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/web/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {
	
	private final AuthService authService;
	private final FeedbackService feedbackService;
	private final ApprovalService approvalService;
	private final ProjectService projectService;
	private final UserService userService;
	private final CommentService commentService;
	private final FeedbackHistoryService feedbackHistoryService;
	
	@GetMapping
	public String getAllFeedbacks(Model model) {
		UserDTO user = authService.getAuthenticatedUserInfo();
		List<FeedbackDto> feedbacks = (isRegularUser(user))
		                              ? feedbackService.getFeedbackByUserId(user.getEmail())
		                              : feedbackService.getAll();
		
		model.addAttribute("feedbackDto", new FeedbackDto());
		model.addAttribute("feedbackList", feedbacks);
		model.addAttribute("user", user);
		model.addAttribute("categories", FeedbackCategory.values());
		model.addAttribute("projects", projectService.getAllProjects());
		model.addAttribute("priorities", FeedbackPriority.values());
		model.addAttribute("statuses", FeedbackStatus.values());
		
		return "pages/feedback/feedback-dashboard";
	}
	
	@PostMapping("/create")
	public String createFeedback(@ModelAttribute FeedbackDto feedbackDto) {
		User user = getAuthenticatedUserEntity();
		feedbackDto.setFeedbackBy(user);
		feedbackDto.setCreatedBy(user.getEmail());
		
		Feedback feedback = feedbackService.create(feedbackDto);
		feedbackHistoryService.create(createFeedbackHistory(feedback, user, "Feedback created"));
		
		return "redirect:/web/feedbacks?success=Feedback created";
	}
	
	@GetMapping("/{id}")
	public String getFeedbackDetails(@PathVariable Long id, Model model) {
		UserDTO user = authService.getAuthenticatedUserInfo();
		FeedbackDto feedbackDto = feedbackService.getFeedbackDtoById(id);
		
		if (isUnauthorizedUser(user, feedbackDto)) {
			return "redirect:/web/feedbacks?error=Unauthorized action";
		}
		
		model.addAttribute("user", user);
		model.addAttribute("canEdit", user.getEmail().equals(feedbackDto.getFeedbackBy().getEmail()));
		model.addAttribute("feedbackDto", feedbackDto);
		model.addAttribute("categories", FeedbackCategory.values());
		model.addAttribute("projects", projectService.getAllProjects());
		model.addAttribute("comments", commentService.getComments(id));
		model.addAttribute("feedbackHistories", feedbackHistoryService.getByFeedbackId(id));
		model.addAttribute("priorities", FeedbackPriority.values());
		model.addAttribute("statuses", isRegularUser(user) ? feedbackDto.getStatus() : FeedbackStatus.values());
		
		return "pages/feedback/one-feedback";
	}
	
	@PostMapping("/{id}")
	public String updateFeedback(@PathVariable Long id, @ModelAttribute FeedbackDto feedbackDto) {
		UserDTO user = authService.getAuthenticatedUserInfo();
		
		if (isUnauthorizedUser(user, feedbackService.getFeedbackDtoById(id))) {
			return "redirect:/web/feedbacks?error=Unauthorized action";
		}
		
		feedbackDto.setUpdatedBy(user.getEmail());
		feedbackDto.setStatus(FeedbackStatus.PENDING);
		feedbackService.update(feedbackDto);
		
		feedbackHistoryService.create(createFeedbackHistory(
				feedbackService.findFeedbackById(id), getAuthenticatedUserEntity(), "Feedback updated"));
		
		return "redirect:/web/feedbacks/{id}?success=Feedback updated";
	}
	
	@GetMapping("/delete/{id}")
	public String deleteFeedback(@PathVariable Long id) {
		UserDTO user = authService.getAuthenticatedUserInfo();
		
		if (isUnauthorizedUser(user, feedbackService.getFeedbackDtoById(id))) {
			return "redirect:/web/feedbacks?error=Unauthorized action";
		}
		
		feedbackService.delete(id, user.getEmail());
		feedbackHistoryService.create(createFeedbackHistory(
				feedbackService.findFeedbackById(id), getAuthenticatedUserEntity(), "Feedback deleted"));
		
		return "redirect:/web/feedbacks?success=Feedback deleted";
	}
	
	@PostMapping("/update-status")
	@ResponseBody
	public ResponseEntity<String> updateFeedbackStatus(@RequestBody FeedbackDto updateDto) {
		UserDTO user = authService.getAuthenticatedUserInfo();
		FeedbackDto feedback = feedbackService.getFeedbackDtoById(updateDto.getId());
		
		if (feedback == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Feedback not found");
		}
		
		feedback.setUpdatedBy(user.getEmail());
		feedback.setStatus(updateDto.getStatus());
		
		ApprovalResponse approval = new ApprovalResponse();
		approval.setApprovedBy(getAuthenticatedUserEntity());
		approval.setFeedbackId(feedback.getId());
		approval.setCreatedBy(user.getEmail());
		approval.setApprovalStatus(updateDto.getStatus().equals(FeedbackStatus.APPROVED));
		
		approvalService.update(approval);
		feedbackService.update(feedback);
		
		feedbackHistoryService.create(createFeedbackHistory(
				feedbackService.findFeedbackById(updateDto.getId()), getAuthenticatedUserEntity(), "Feedback Status Updated"));
		
		return ResponseEntity.ok("Status updated successfully");
	}
	
	private boolean isRegularUser(UserDTO user) {
		return user != null && user.getRole().equals(UserRole.USER);
	}
	
	private boolean isUnauthorizedUser(UserDTO user, FeedbackDto feedbackDto) {
		return isRegularUser(user) && !feedbackDto.getFeedbackBy().getId().equals(user.getId());
	}
	
	private User getAuthenticatedUserEntity() {
		return userService.getUserEntityById(authService.getAuthenticatedUserInfo().getId());
	}
	
	//	private FeedbackHistoryResponse createFeedbackHistory(Feedback feedback, User user, String changeDescription) {
//		return FeedbackHistoryResponse.builder()
//		                              .createdBy(user.getEmail())
//		                              .changedBy(user)
//		                              .feedback(feedback)
//		                              .feedbackId(feedback.getId())
//		                              .changeDescription(changeDescription)
//		                              .createdAt(LocalDateTime.now())
//		                              .isDeleted(false)
//		                              .build();
//	}
	private FeedbackHistoryResponse createFeedbackHistory(Feedback feedback, User user, String changeDescription) {
		FeedbackHistoryResponse feedbackHistoryResponse = new FeedbackHistoryResponse();
		feedbackHistoryResponse.setCreatedBy(user.getEmail());
		feedbackHistoryResponse.setChangedBy(user);
		feedbackHistoryResponse.setFeedback(feedback);
		feedbackHistoryResponse.setFeedbackId(feedback.getId());
		feedbackHistoryResponse.setChangeDescription(changeDescription);
		feedbackHistoryResponse.setCreatedAt(LocalDateTime.now());
		feedbackHistoryResponse.setIsDeleted(Boolean.FALSE);
		return feedbackHistoryResponse;
	}
}
