package sams.feedbloom.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sams.feedbloom.project.dto.ProjectRequest;
import sams.feedbloom.project.dto.ProjectResponse;
import sams.feedbloom.project.entity.Project;
import sams.feedbloom.project.repository.ProjectRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
	
	private final ProjectRepository projectRepository;
	
	@Transactional
	public void createProject(ProjectRequest request) {
		Project project = new Project();
		project.setName(request.getName());
		project.setCreatedBy(request.getCreatedBy());
		projectRepository.save(project);
	}
	
	public void updateProject(ProjectRequest request) {
		Project project = projectRepository.findById(request.getId())
		                                   .orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + request.getId()));
		project.setName(request.getName());
		project.setUpdatedAt(LocalDateTime.now());
		projectRepository.save(project);
	}
	
	public List<ProjectResponse> getAllProjects() {
		return projectRepository.findAll()
		                        .stream()
		                        .map(this::mapToResponse)
		                        .toList();
	}
	
	public ProjectResponse getProjectById(Long id) {
		return projectRepository.findById(id)
		                        .map(this::mapToResponse)
		                        .orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + id));
	}
	
	@Transactional
	public void deleteProject(Long id) {
		projectRepository.findById(id).ifPresentOrElse(
				projectRepository::delete,
				() -> {throw new IllegalArgumentException("Project not found with ID: " + id);}
		                                              );
	}
	
	private ProjectResponse mapToResponse(Project project) {
		ProjectResponse response = new ProjectResponse();
		response.setId(project.getId());
		response.setName(project.getName());
		response.setCreatedBy(project.getCreatedBy());
		return response;
	}
}
