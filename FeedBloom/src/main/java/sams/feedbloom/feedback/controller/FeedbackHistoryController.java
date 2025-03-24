package sams.feedbloom.feedback.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import sams.feedbloom.feedback.service.FeedbackHistoryService;

@Controller
@RequestMapping("/feedback-histories")
@RequiredArgsConstructor
public class FeedbackHistoryController {
	
	private final FeedbackHistoryService feedbackHistoryService;
	
	@GetMapping("/{id}")
	public String getById(@PathVariable Long id, Model model) {
		model.addAttribute("history", feedbackHistoryService.getById(id));
		return "feedback-history/details";
	}
}