package sams.feedbloom.feedback.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sams.feedbloom.feedback.dto.FeedbackAttachmentResponse;
import sams.feedbloom.feedback.service.FeedbackAttachmentService;

import java.util.List;

@Controller
@RequestMapping("/feedback-attachments")
@RequiredArgsConstructor
public class FeedbackAttachmentController {
	
	private final FeedbackAttachmentService feedbackAttachmentService;
	
	@GetMapping("/{id}")
	public String getById(@PathVariable Long id, Model model) {
		model.addAttribute("attachment", feedbackAttachmentService.getById(id));
		return "feedback-attachment/details";
	}
	
	@GetMapping
	public String getAll(Model model) {
		List<FeedbackAttachmentResponse> attachmentList = feedbackAttachmentService.getAll();
		model.addAttribute("attachmentList", attachmentList);
		return "feedback-attachment/list";
	}
	
	@GetMapping("/create")
	public String showCreateForm(Model model) {
		model.addAttribute("attachment", new FeedbackAttachmentResponse());
		return "feedback-attachment/form";
	}
	
	@PostMapping("/create")
	public String create(@ModelAttribute FeedbackAttachmentResponse attachmentResponse) {
		feedbackAttachmentService.create(attachmentResponse);
		return "redirect:/feedback-attachments";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Long id) {
		feedbackAttachmentService.delete(id);
		return "redirect:/feedback-attachments";
	}
}