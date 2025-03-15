package sams.feedbloom.feedback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import sams.feedbloom.common.dto.CommonDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FeedbackAttachmentResponse extends CommonDTO {
	private Long id;
	private Long feedbackId;
	private String attachmentUrl;
}