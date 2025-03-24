package sams.feedbloom.comment.dto;

import lombok.Getter;
import lombok.Setter;
import sams.feedbloom.common.dto.CommonDTO;
import sams.feedbloom.user.entity.User;

@Getter
@Setter
public class CommentDto extends CommonDTO {
	private Long id;
	private Long feedbackId;
	private String content;
	private User user;
}
