package sams.feedbloom.feedback.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sams.feedbloom.feedback.dto.FeedbackResponse;
import sams.feedbloom.feedback.service.FeedbackService;

import java.util.List;

@Controller
@RequestMapping("/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {
	
	private final FeedbackService feedbackService;
	
	@GetMapping("/{id}")
	public String getById(@PathVariable Long id, Model model) {
		model.addAttribute("feedback", feedbackService.getById(id));
		return "feedback/details";
	}
	
	@GetMapping
	public String getAll(Model model) {
		List<FeedbackResponse> feedbackList = feedbackService.getAll();
		model.addAttribute("feedbackList", feedbackList);
		return "feedback/list";
	}
	
	@GetMapping("/create")
	public String showCreateForm(Model model) {
		model.addAttribute("feedback", new FeedbackResponse());
		return "feedback/form";
	}
	
	@PostMapping("/create")
	public String create(@ModelAttribute FeedbackResponse feedbackResponse) {
		feedbackService.create(feedbackResponse);
		return "redirect:/feedbacks";
	}
	
	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable Long id, Model model) {
		model.addAttribute("feedback", feedbackService.getById(id));
		return "feedback/form";
	}
	
	@PostMapping("/edit/{id}")
	public String update(@PathVariable Long id, @ModelAttribute FeedbackResponse feedbackResponse) {
		feedbackService.update(id, feedbackResponse);
		return "redirect:/feedbacks";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Long id) {
		feedbackService.delete(id);
		return "redirect:/feedbacks";
	}
}
