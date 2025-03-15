package sams.feedbloom.feedback.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import sams.feedbloom.feedback.dto.ApprovalResponse;
import sams.feedbloom.feedback.service.ApprovalService;

import java.util.List;

@Controller
@RequestMapping("/approvals")
@RequiredArgsConstructor
public class ApprovalController {
	
	private final ApprovalService approvalService;
	
	@GetMapping("/{id}")
	public String getById(@PathVariable Long id, Model model) {
		model.addAttribute("approval", approvalService.getById(id));
		return "approval/details";
	}
	
	@GetMapping
	public String getAll(Model model) {
		List<ApprovalResponse> approvalList = approvalService.getAll();
		model.addAttribute("approvalList", approvalList);
		return "approval/list";
	}
	
	@PostMapping("/approve/{id}")
	public String approve(@PathVariable Long id) {
		approvalService.approve(id);
		return "redirect:/approvals";
	}
}