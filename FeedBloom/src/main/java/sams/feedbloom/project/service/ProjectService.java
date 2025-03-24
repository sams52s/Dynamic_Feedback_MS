package sams.feedbloom.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sams.feedbloom.project.dto.ProjectDto;
import sams.feedbloom.project.entity.Project;
import sams.feedbloom.project.repository.ProjectRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {
	
	private final ProjectRepository projectRepository;
	
	@Transactional
	public void createProject(ProjectDto request) {
		Project project = new Project();
		project.setName(request.getName());
		project.setCreatedBy(request.getCreatedBy());
		projectRepository.save(project);
	}
	
	public void updateProject(ProjectDto request) {
		Project project = projectRepository.findById(request.getId())
		                                   .orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + request.getId()));
		project.setName(request.getName());
		project.setUpdatedAt(LocalDateTime.now());
		project.setUpdatedBy(request.getUpdatedBy());
		projectRepository.save(project);
	}
	
	public List<ProjectDto> getAllProjects() {
		return projectRepository.findByIsDeletedFalse()
		                        .stream()
		                        .map(this::mapToResponse)
		                        .toList();
	}
	
	public ProjectDto getProjectById(Long id) {
		return projectRepository.findById(id)
		                        .map(this::mapToResponse)
		                        .orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + id));
	}
	
	public Project getProjectEntityById(Long id) {
		return projectRepository.findById(id)
		                        .orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + id));
	}
	
	@Transactional
	public void deleteProject(Long id, String deletedBy) {
		Project project = projectRepository.findById(id)
		                                   .orElseThrow(() -> new IllegalArgumentException("Project not found with ID: " + id));
		
		project.setDeletedAt(LocalDateTime.now());
		project.setDeletedBy(deletedBy);
		project.setIsDeleted(true);
		projectRepository.save(project);
	}
	
	private ProjectDto mapToResponse(Project project) {
		ProjectDto projectDto = new ProjectDto();
		projectDto.setId(project.getId());
		projectDto.setName(project.getName());
		projectDto.setCreatedBy(project.getCreatedBy());
		projectDto.setCreatedAt(project.getCreatedAt());
		projectDto.setUpdatedAt(project.getUpdatedAt());
		projectDto.setUpdatedBy(project.getUpdatedBy());
		projectDto.setDeletedAt(project.getDeletedAt());
		projectDto.setIsDeleted(project.getIsDeleted());
		
		return projectDto;
	}
}
