package sams.feedbloom.project.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sams.feedbloom.common.dto.CommonDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto extends CommonDTO {
	@NotBlank(message = "Project name cannot be blank")
	private String name;
	private Long id;
}
