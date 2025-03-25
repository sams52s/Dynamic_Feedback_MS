package sams.feedbloom.feedback.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sams.feedbloom.feedback.dto.FeedbackHistoryResponse;
import sams.feedbloom.feedback.entity.FeedbackHistory;
import sams.feedbloom.feedback.repository.FeedbackHistoryRepository;

import java.util.Comparator;
import java.util.LinkedList;
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
	
	public LinkedList<FeedbackHistoryResponse> getByFeedbackId(Long feedbackId) {
		return feedbackHistoryRepository.findByIsDeletedFalseAndFeedbackIdOrderByCreatedAtAsc(feedbackId)
		                                .stream()
		                                .sorted(Comparator.comparing(FeedbackHistory::getCreatedAt).reversed())
		                                .map(this::mapToResponse)
		                                .collect(Collectors.toCollection(LinkedList::new));
	}
	
	public List<FeedbackHistoryResponse> getAll() {
		return feedbackHistoryRepository.findAll().stream()
		                                .map(this::mapToResponse)
		                                .collect(Collectors.toList());
	}
	
	public void create(FeedbackHistoryResponse feedbackHistoryResponse) {
		feedbackHistoryRepository.save(mapToEntity(feedbackHistoryResponse, new FeedbackHistory()));
	}
	
	private FeedbackHistoryResponse mapToResponse(FeedbackHistory history) {
		FeedbackHistoryResponse response = new FeedbackHistoryResponse();
		response.setId(history.getId());
		response.setCreatedAt(history.getCreatedAt());
		response.setCreatedBy(history.getCreatedBy());
		response.setFeedbackId(history.getFeedback().getId());
		response.setFeedback(history.getFeedback());
		response.setChangeDescription(history.getChangeDescription());
		response.setChangedBy(history.getChangedBy());
		response.setUpdatedAt(history.getUpdatedAt());
		response.setIsDeleted(history.getIsDeleted());
		response.setDeletedAt(history.getDeletedAt());
		response.setDeletedBy(history.getDeletedBy());
		return response;
	}
	
	private FeedbackHistory mapToEntity(FeedbackHistoryResponse response, FeedbackHistory history) {
		history.setFeedback(response.getFeedback());
		history.setChangedBy(response.getChangedBy());
		history.setChangeDescription(response.getChangeDescription());
		history.setCreatedAt(response.getCreatedAt());
		history.setCreatedBy(response.getCreatedBy());
		history.setUpdatedAt(response.getUpdatedAt());
		history.setIsDeleted(response.getIsDeleted());
		history.setDeletedAt(response.getDeletedAt());
		history.setDeletedBy(response.getDeletedBy());
		return history;
	}
}