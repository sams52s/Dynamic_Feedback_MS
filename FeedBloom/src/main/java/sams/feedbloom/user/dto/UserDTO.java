package sams.feedbloom.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sams.feedbloom.common.dto.CommonDTO;
import sams.feedbloom.user.entity.UserRole;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO extends CommonDTO {
	private Long id;
	private String name;
	private String email;
	private UserRole role;
}
