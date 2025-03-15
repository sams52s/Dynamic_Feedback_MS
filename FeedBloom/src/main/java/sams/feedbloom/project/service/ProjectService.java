package sams.feedbloom.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sams.feedbloom.project.dto.ProjectRequest;
import sams.feedbloom.project.dto.ProjectResponse;
import sams.feedbloom.project.entity.Project;
import sams.feedbloom.project.repository.ProjectRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectService {
	
	private final ProjectRepository projectRepository;
	
	@Transactional
	public ProjectResponse createProject(ProjectRequest request) {
		Project project = new Project();
		project.setName(request.getName());
		project.setCreatedBy(request.getCreatedBy());
		
		Project savedProject = projectRepository.save(project);
		return mapToResponse(savedProject);
	}
	
	public List<ProjectResponse> getAllProjects() {
		List<Project> projects = projectRepository.findAll();
		return projects.stream().map(this::mapToResponse).collect(Collectors.toList());
	}
	
	public ProjectResponse getProjectById(Long id) {
		Project project = projectRepository.findById(id)
		                                   .orElseThrow(() -> new RuntimeException("Project not found with ID: " + id));
		return mapToResponse(project);
	}
	
	@Transactional
	public void deleteProject(Long id) {
		projectRepository.deleteById(id);
	}
	
	private ProjectResponse mapToResponse(Project project) {
		ProjectResponse response = new ProjectResponse();
		response.setId(project.getId());
		response.setName(project.getName());
		response.setCreatedBy(project.getCreatedBy());
		return response;
	}
}
