package sams.feedbloom.project.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sams.feedbloom.authentication.service.AuthService;
import sams.feedbloom.project.dto.ProjectDto;
import sams.feedbloom.project.service.ProjectService;
import sams.feedbloom.user.dto.UserDTO;

import java.util.List;

@Controller
@RequestMapping("/web/project")
@RequiredArgsConstructor
public class ProjectController {
	private final AuthService authService;
	private final ProjectService projectService;
	
	@GetMapping("/dashboard")
	public String showDashboard(Model model) {
		UserDTO userInfo = authService.getAuthenticatedUserInfo();
		model.addAttribute("user", userInfo);
		List<ProjectDto> projectList = projectService.getAllProjects();
		model.addAttribute("request", new ProjectDto());
		model.addAttribute("projectList", projectList);
		return "pages/common/projects";
	}
	
	@PostMapping("/create")
	public String createProject(@Valid @ModelAttribute ProjectDto request) {
		if (request.getName().isBlank()) {
			throw new IllegalArgumentException("Project name cannot be blank");
		}
		UserDTO userInfo = authService.getAuthenticatedUserInfo();
		request.setCreatedBy(userInfo.getName());
		projectService.createProject(request);
		return "redirect:/web/project/dashboard";
	}
	
	@PostMapping("/update")
	public String updateProject(@Valid @ModelAttribute ProjectDto request, RedirectAttributes redirectAttributes) {
		if (request.getName().isBlank()) {
			redirectAttributes.addFlashAttribute("error", "Project name cannot be blank");
			return "redirect:/web/project/dashboard";
		}
		
		if (projectService.getProjectById(request.getId()) == null) {
			redirectAttributes.addFlashAttribute("error", "Project not found");
			return "redirect:/web/project/dashboard";
		}
		UserDTO userInfo = authService.getAuthenticatedUserInfo();
		
		ProjectDto projectDto = projectService.getProjectById(request.getId());
		projectDto.setName(request.getName());
		projectDto.setUpdatedBy(userInfo.getEmail());
		projectService.updateProject(projectDto);
		redirectAttributes.addFlashAttribute("success", "Project updated successfully");
		return "redirect:/web/project/dashboard";
	}
	
	@GetMapping("/{id}/delete")
	public String deleteProject(@PathVariable Long id) {
		UserDTO userInfo = authService.getAuthenticatedUserInfo();
		ProjectDto projectDto = projectService.getProjectById(id);
		if (projectDto == null) {
			throw new IllegalArgumentException("Project not found with ID: " + id);
		}
		projectService.deleteProject(id, userInfo.getEmail());
		return "redirect:/web/project/dashboard";
	}
}
