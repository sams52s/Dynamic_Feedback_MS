package sams.feedbloom.feedback.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sams.feedbloom.authentication.service.AuthService;
import sams.feedbloom.comment.dto.CommentDto;
import sams.feedbloom.comment.service.CommentService;
import sams.feedbloom.feedback.dto.ApprovalResponse;
import sams.feedbloom.feedback.dto.FeedbackDto;
import sams.feedbloom.feedback.entity.FeedbackCategory;
import sams.feedbloom.feedback.entity.FeedbackPriority;
import sams.feedbloom.feedback.entity.FeedbackStatus;
import sams.feedbloom.feedback.service.ApprovalService;
import sams.feedbloom.feedback.service.FeedbackService;
import sams.feedbloom.project.dto.ProjectDto;
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
	
	@GetMapping
	public String getAll(Model model) {
		UserDTO userInfo = authService.getAuthenticatedUserInfo();
		List<ProjectDto> projectList = projectService.getAllProjects();
		List<FeedbackDto> feedbackList;
		if (userInfo != null && userInfo.getRole().equals(UserRole.USER)) {
			feedbackList = feedbackService.getFeedbackByUserId(userInfo.getEmail());
		} else {
			feedbackList = feedbackService.getAll();
		}
		LocalDateTime now = LocalDateTime.now();
		
		model.addAttribute("feedbackDto", new FeedbackDto());
		model.addAttribute("feedbackList", feedbackList);
		model.addAttribute("user", userInfo);
		model.addAttribute("categories", FeedbackCategory.values());
		model.addAttribute("projects", projectList);
		model.addAttribute("priorities", FeedbackPriority.values());
		model.addAttribute("statuses", FeedbackStatus.values());
		return "pages/feedback/feedback-dashboard";
	}
	
	@PostMapping("/create")
	public String create(@ModelAttribute FeedbackDto feedbackDto) {
		User user = userService.getUserEntityById(authService.getAuthenticatedUserInfo().getId());
		feedbackDto.setFeedbackBy(user);
		feedbackDto.setCreatedBy(user.getEmail());
		feedbackService.create(feedbackDto);
		return "redirect:/web/feedbacks?success=Feedback created";
	}
	
	@GetMapping("/{id}")
	public String getById(@PathVariable Long id, Model model) {
		UserDTO userInfo = authService.getAuthenticatedUserInfo();
		List<ProjectDto> projectList = projectService.getAllProjects();
		FeedbackDto feedbackDto = feedbackService.getById(id);
		List<CommentDto> commentList = commentService.getComments(id);
		if (userInfo.getRole().equals(UserRole.USER) && !feedbackDto.getFeedbackBy().getId().equals(userInfo.getId())) {
			return "redirect:/web/feedbacks?error=Unauthorized action";
		}
		if (userInfo.getRole().equals(UserRole.USER)) {
			model.addAttribute("statuses", feedbackDto.getStatus());
		} else {
			model.addAttribute("statuses", FeedbackStatus.values());
		}
		Boolean canEdit = userInfo.getEmail().equals(feedbackDto.getFeedbackBy().getEmail());
		model.addAttribute("user", userInfo);
		model.addAttribute("canEdit", canEdit);
		model.addAttribute("feedbackDto", feedbackDto);
		model.addAttribute("categories", FeedbackCategory.values());
		model.addAttribute("projects", projectList);
		model.addAttribute("comments", commentList);
		model.addAttribute("priorities", FeedbackPriority.values());
		return "pages/feedback/one-feedback";
	}
	
	
	@PostMapping("/{id}")
	public String update(@PathVariable Long id, @ModelAttribute FeedbackDto feedbackDto) {
		UserDTO userInfo = authService.getAuthenticatedUserInfo();
		feedbackDto.setUpdatedBy(userInfo.getEmail());
		feedbackDto.setStatus(FeedbackStatus.PENDING);
		if (!feedbackService.getById(id).getFeedbackBy().getId().equals(userInfo.getId())) {
			return "redirect:/web/feedbacks?error=Unauthorized action";
		}
		feedbackService.update(feedbackDto);
		return "redirect:/web/feedbacks/{id}?success=Feedback updated";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Long id) {
		UserDTO userInfo = authService.getAuthenticatedUserInfo();
		FeedbackDto existingFeedback = feedbackService.getById(id);
		if (!existingFeedback.getFeedbackBy().getId().equals(userInfo.getId())) {
			return "redirect:/web/feedbacks?error=Unauthorized action";
		}
		
		feedbackService.delete(id, userInfo.getEmail());
		return "redirect:/web/feedbacks?success=Feedback deleted";
	}
	
	@PostMapping("/update-status")
	@ResponseBody
	public ResponseEntity<String> updateFeedbackStatus(@RequestBody FeedbackDto updateDto) {
		UserDTO userInfo = authService.getAuthenticatedUserInfo();
		
		FeedbackDto feedback = feedbackService.getById(updateDto.getId());
		if (feedback == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Feedback not found");
		}
		feedback.setUpdatedBy(userInfo.getEmail());
		feedback.setStatus(updateDto.getStatus());
		
		ApprovalResponse approval = new ApprovalResponse();
		approval.setApprovedBy((userService.getUserEntityById(userInfo.getId())));
		approval.setFeedbackId(feedback.getId());
		approval.setCreatedBy(userInfo.getEmail());
		
		approval.setApprovalStatus(updateDto.getStatus().equals(FeedbackStatus.APPROVED));
		approvalService.update(approval);
		feedbackService.update(feedback);
		
		return ResponseEntity.ok("Status updated successfully");
	}
}
