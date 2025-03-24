package sams.feedbloom.feedback.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sams.feedbloom.feedback.dto.FeedbackDto;
import sams.feedbloom.feedback.entity.Feedback;
import sams.feedbloom.feedback.entity.FeedbackStatus;
import sams.feedbloom.feedback.mapper.FeedbackMapper;
import sams.feedbloom.feedback.repository.FeedbackRepository;
import sams.feedbloom.project.entity.Project;
import sams.feedbloom.project.repository.ProjectRepository;
import sams.feedbloom.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {
	
	private final FeedbackRepository feedbackRepository;
	private final UserRepository userRepository;
	private final ProjectRepository projectRepository;
	
	
	public FeedbackDto getById(Long id) {
		Feedback feedback = feedbackRepository.findByIsDeletedFalseAndId(id);
		return mapToDto(feedback);
	}
	
	public List<FeedbackDto> getAll() {
		return getSortedFeedbackDto(feedbackRepository.findByIsDeletedFalse());
	}
	
	public List<FeedbackDto> getFeedbackByUserId(String createdBy) {
		return getSortedFeedbackDto(feedbackRepository.findByIsDeletedAndCreatedByOrderByCreatedAtAsc(Boolean.FALSE, createdBy));
	}
	
	public void create(FeedbackDto feedbackDto) {
		feedbackDto.setCreatedAt(LocalDateTime.now());
		feedbackDto.setStatus(FeedbackStatus.PENDING);
		feedbackDto.setIsDeleted(Boolean.FALSE);
		
		validateFeedbackDto(feedbackDto);
		
		Project project = findProjectById(feedbackDto.getProjectId());
		feedbackDto.setProject(project);
		Feedback feedback = new Feedback();
		
		feedbackRepository.save(FeedbackMapper.mapToEntity(feedbackDto, feedback));
	}
	
	public void update(FeedbackDto feedbackDto) {
		feedbackDto.setUpdatedAt(LocalDateTime.now());
		feedbackDto.setIsDeleted(Boolean.FALSE);
		Project project = findProjectById(feedbackDto.getProjectId());
		feedbackDto.setProject(project);
		
		validateFeedbackDto(feedbackDto);
		Feedback feedback = feedbackRepository.findByIsDeletedFalseAndId(feedbackDto.getId());
		feedbackDto.setFeedbackBy(feedback.getFeedbackBy());
		feedbackDto.setCreatedBy(feedback.getCreatedBy());
		feedbackDto.setCreatedAt(feedback.getCreatedAt());
		feedbackRepository.save(FeedbackMapper.mapToEntity(feedbackDto, feedback));
	}
	
	public void delete(Long id, String email) {
		Feedback feedback = findFeedbackById(id);
		feedback.setIsDeleted(true);
		feedback.setDeletedBy(email);
		feedback.setDeletedAt(LocalDateTime.now());
		
		feedbackRepository.save(feedback);
	}
	
	private List<FeedbackDto> getSortedFeedbackDto(List<Feedback> feedbackList) {
		return feedbackList.stream()
		                   .sorted(Comparator.comparing(Feedback::getCreatedAt).reversed())
		                   .map(this::mapToDto)
		                   .toList();
	}
	
	private FeedbackDto mapToDto(Feedback feedback) {
		FeedbackDto dto = FeedbackMapper.mapToResponse(feedback);
		dto.setProjectName(feedback.getProject().getName());
		return dto;
	}
	
	private Feedback findFeedbackById(Long id) {
		return feedbackRepository.findById(id)
		                         .orElseThrow(() -> new EntityNotFoundException("Feedback not found with ID: " + id));
	}
	
	
	private Project findProjectById(Long projectId) {
		return projectRepository.findById(projectId)
		                        .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + projectId));
	}
	
	private void validateFeedbackDto(FeedbackDto feedbackDto) {
		if (feedbackDto.getTitle() == null || feedbackDto.getTitle().trim().isEmpty()) {
			throw new IllegalArgumentException("Title cannot be null or empty.");
		}
		if (feedbackDto.getDescription() == null || feedbackDto.getDescription().trim().isEmpty()) {
			throw new IllegalArgumentException("Description cannot be null or empty.");
		}
		if (feedbackDto.getCategory() == null) {
			throw new IllegalArgumentException("Category cannot be null.");
		}
		if (feedbackDto.getPriority() == null) {
			throw new IllegalArgumentException("Priority cannot be null.");
		}
	}
}
