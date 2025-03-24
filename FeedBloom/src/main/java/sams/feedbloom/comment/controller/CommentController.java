package sams.feedbloom.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sams.feedbloom.authentication.service.AuthService;
import sams.feedbloom.comment.dto.CommentDto;
import sams.feedbloom.comment.service.CommentService;
import sams.feedbloom.user.dto.UserDTO;

import java.util.List;

@Controller
@RequestMapping("/web/comments")
@RequiredArgsConstructor
public class CommentController {
	private final CommentService commentService;
	private final AuthService authService;
	
	@ResponseBody
	@GetMapping("/{feedbackId}")
	public List<CommentDto> getComments(@PathVariable Long feedbackId) {
		return commentService.getComments(feedbackId);
	}
	
	@PostMapping("/{feedbackId}")
	public String addComment(@PathVariable Long feedbackId,
	                         @RequestParam("content") String content,
	                         RedirectAttributes redirectAttributes) {
		UserDTO userInfo = authService.getAuthenticatedUserInfo();
		commentService.addComment(feedbackId, userInfo.getId(), content);
		redirectAttributes.addFlashAttribute("success", "Comment added successfully!");
		
		return "redirect:/web/feedbacks/" + feedbackId;
	}
	
	@PostMapping("/{feedbackId}/{commentId}/update-comment")
	public String updateComment(@PathVariable Long feedbackId,
	                            @PathVariable Long commentId,
	                            @RequestParam("content") String updatedContent,
	                            RedirectAttributes redirectAttributes) {
		UserDTO userInfo = authService.getAuthenticatedUserInfo();
		commentService.updateComment(commentId, updatedContent, userInfo.getId());
		
		redirectAttributes.addFlashAttribute("success", "Comment updated successfully!");
		return "redirect:/web/feedbacks/" + feedbackId;
	}
	
	@GetMapping("/{feedbackId}/{commentId}/delete")
	public String deleteComment(@PathVariable Long feedbackId,
	                            @PathVariable Long commentId,
	                            RedirectAttributes redirectAttributes) {
		commentService.deleteComment(commentId);
		redirectAttributes.addFlashAttribute("success", "Comment deleted successfully!");
		
		return "redirect:/web/feedbacks/" + feedbackId;
	}
}
