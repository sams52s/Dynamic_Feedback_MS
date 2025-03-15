package sams.feedbloom.feedback.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sams.feedbloom.feedback.dto.FeedbackResponse;
import sams.feedbloom.feedback.entity.Feedback;
import sams.feedbloom.feedback.mapper.FeedbackMapper;
import sams.feedbloom.feedback.repository.FeedbackRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class FeedbackService {
	
	private final FeedbackRepository feedbackRepository;
	
	public FeedbackResponse getById(Long id) {
		Feedback feedback = feedbackRepository.findById(id)
		                                      .orElseThrow(() -> new RuntimeException("Feedback not found with id: " + id));
		
		return FeedbackMapper.mapToResponse(feedback);
	}
	
	public List<FeedbackResponse> getAll() {
		return feedbackRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream()
		                         .map(FeedbackMapper::mapToResponse)
		                         .collect(Collectors.toList());
	}
	
	public void create(FeedbackResponse feedbackResponse) {
		Feedback feedback = FeedbackMapper.mapToEntity(feedbackResponse);
		feedbackRepository.save(feedback);
	}
	
	public void update(Long id, FeedbackResponse feedbackResponse) {
		Feedback feedback = feedbackRepository.findById(id)
		                                      .orElseThrow(() -> new RuntimeException("Feedback not found with id: " + id));
		feedback.setTitle(feedbackResponse.getTitle());
		feedback.setDescription(feedbackResponse.getDescription());
		feedbackRepository.save(feedback);
	}
	
	public void delete(Long id) {
		feedbackRepository.deleteById(id);
	}
	
}