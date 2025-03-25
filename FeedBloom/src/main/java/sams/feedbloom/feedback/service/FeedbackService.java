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

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackService {
	
	private final FeedbackRepository feedbackRepository;
	private final ProjectRepository projectRepository;
	
	public List<FeedbackDto> getAll() {
		return feedbackRepository.findByIsDeletedFalse().stream()
		                         .sorted(Comparator.comparing(Feedback::getCreatedAt).reversed())
		                         .map(FeedbackMapper::mapToResponse)
		                         .collect(Collectors.toList());
	}
	
	public List<FeedbackDto> getFeedbackByUserId(String createdBy) {
		return feedbackRepository.findByIsDeletedAndCreatedByOrderByCreatedAtAsc(Boolean.FALSE, createdBy).stream()
		                         .map(FeedbackMapper::mapToResponse)
		                         .collect(Collectors.toList());
	}
	
	public FeedbackDto getFeedbackDtoById(Long id) {
		return FeedbackMapper.mapToResponse(findFeedbackById(id));
	}
	
	public Feedback create(FeedbackDto feedbackDto) {
		setDefaultValues(feedbackDto);
		validateFeedbackDto(feedbackDto);
		feedbackDto.setProject(findProjectById(feedbackDto.getProjectId()));
		
		Feedback feedback = FeedbackMapper.mapToEntity(feedbackDto, new Feedback());
		return feedbackRepository.save(feedback);
	}
	
	public void update(FeedbackDto feedbackDto) {
		validateFeedbackDto(feedbackDto);
		
		Feedback existingFeedback = findFeedbackById(feedbackDto.getId());
		feedbackDto.setUpdatedAt(LocalDateTime.now());
		feedbackDto.setProject(findProjectById(feedbackDto.getProjectId()));
		
		feedbackDto.setFeedbackBy(existingFeedback.getFeedbackBy());
		feedbackDto.setCreatedBy(existingFeedback.getCreatedBy());
		feedbackDto.setCreatedAt(existingFeedback.getCreatedAt());
		
		feedbackRepository.save(FeedbackMapper.mapToEntity(feedbackDto, existingFeedback));
	}
	
	public void delete(Long id, String email) {
		Feedback feedback = findFeedbackById(id);
		feedback.setIsDeleted(true);
		feedback.setDeletedBy(email);
		feedback.setDeletedAt(LocalDateTime.now());
		
		feedbackRepository.save(feedback);
	}
	
	private void setDefaultValues(final FeedbackDto feedbackDto) {
		feedbackDto.setCreatedAt(LocalDateTime.now());
		feedbackDto.setStatus(FeedbackStatus.PENDING);
		feedbackDto.setIsDeleted(Boolean.FALSE);
	}
	
	public Feedback findFeedbackById(Long id) {
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
