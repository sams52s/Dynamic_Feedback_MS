package sams.feedbloom.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sams.feedbloom.project.dto.ProjectRequest;
import sams.feedbloom.project.service.ProjectService;

@Controller
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {
	
	private final ProjectService projectService;
	
	@GetMapping
	public String getAllProjects(Model model) {
		model.addAttribute("projects", projectService.getAllProjects());
		return "projects/list"; // Returns projects/list.html or list.jsp
	}
	
	@GetMapping("/{id}")
	public String getProjectById(@PathVariable Long id, Model model) {
		model.addAttribute("project", projectService.getProjectById(id));
		return "projects/details"; // Returns projects/details.html or details.jsp
	}
	
	@GetMapping("/new")
	public String showCreateForm(Model model) {
		model.addAttribute("project", new ProjectRequest());
		return "projects/form"; // Returns projects/form.html or form.jsp
	}
	
	@PostMapping
	public String createProject(@ModelAttribute ProjectRequest request) {
		projectService.createProject(request);
		return "redirect:/projects"; // Redirects back to project list
	}
	
	@GetMapping("/{id}/delete")
	public String deleteProject(@PathVariable Long id) {
		projectService.deleteProject(id);
		return "redirect:/projects";
	}
}
