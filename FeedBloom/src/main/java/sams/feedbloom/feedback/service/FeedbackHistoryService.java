package sams.feedbloom.feedback.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sams.feedbloom.feedback.dto.FeedbackHistoryResponse;
import sams.feedbloom.feedback.entity.FeedbackHistory;
import sams.feedbloom.feedback.repository.FeedbackHistoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeedbackHistoryService {
	
	private final FeedbackHistoryRepository feedbackHistoryRepository;
	
	public FeedbackHistoryResponse getById(Long id) {
		FeedbackHistory history = feedbackHistoryRepository.findById(id)
		                                                   .orElseThrow(() -> new RuntimeException("History not found with id: " + id));
		return mapToResponse(history);
	}
	
	public List<FeedbackHistoryResponse> getAll() {
		return feedbackHistoryRepository.findAll().stream()
		                                .map(this::mapToResponse)
		                                .collect(Collectors.toList());
	}
	
	private FeedbackHistoryResponse mapToResponse(FeedbackHistory history) {
		FeedbackHistoryResponse response = new FeedbackHistoryResponse();
		response.setId(history.getId());
		response.setFeedbackId(history.getFeedback().getId());
		response.setChangedBy(history.getChangedBy().getId());
		response.setChangeDescription(history.getChangeDescription());
		return response;
	}
}