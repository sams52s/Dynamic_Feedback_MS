package sams.feedbloom.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sams.feedbloom.comment.dto.CommentDto;
import sams.feedbloom.comment.entity.Comment;
import sams.feedbloom.comment.repository.CommentRepository;
import sams.feedbloom.feedback.entity.Feedback;
import sams.feedbloom.feedback.repository.FeedbackRepository;
import sams.feedbloom.user.entity.User;
import sams.feedbloom.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
	private final CommentRepository commentRepository;
	private final FeedbackRepository feedbackRepository;
	private final UserRepository userRepository;
	
	public List<CommentDto> getComments(Long feedbackId) {
		return commentRepository.findByFeedbackIdOrderByCreatedAtDescCreatedAt(feedbackId)
		                        .stream()
		                        .map(this::convertToDto)
		                        .collect(Collectors.toList());
	}
	
	@Transactional
	public CommentDto addComment(Long feedbackId, Long userId, String content) {
		User user = userRepository.findById(userId)
		                          .orElseThrow(() -> new RuntimeException("User not found"));
		Feedback feedback = feedbackRepository.findById(feedbackId)
		                                      .orElseThrow(() -> new RuntimeException("Feedback not found"));
		
		Comment comment = new Comment();
		comment.setUser(user);
		comment.setFeedback(feedback);
		comment.setContent(content);
		
		comment.setCreatedBy(user.getEmail());
		
		Comment savedComment = commentRepository.save(comment);
		return convertToDto(savedComment);
	}
	
	@Transactional
	public CommentDto updateComment(Long commentId, String newContent, Long userId) {
		Comment comment = commentRepository.findById(commentId)
		                                   .orElseThrow(() -> new RuntimeException("Comment not found"));
		User user = userRepository.findById(userId)
		                          .orElseThrow(() -> new RuntimeException("User not found"));
		comment.setUpdatedBy(user.getEmail());
		comment.setUpdatedAt(LocalDateTime.now());
		comment.setContent(newContent);
		return convertToDto(commentRepository.save(comment));
	}
	
	@Transactional
	public void deleteComment(Long commentId) {
		commentRepository.deleteById(commentId);
	}
	
	private CommentDto convertToDto(Comment comment) {
		CommentDto dto = new CommentDto();
		dto.setId(comment.getId());
		dto.setFeedbackId(comment.getFeedback().getId());
		dto.setContent(comment.getContent());
		dto.setUser(comment.getUser());
		dto.setCreatedAt(comment.getCreatedAt());
		return dto;
	}
}
