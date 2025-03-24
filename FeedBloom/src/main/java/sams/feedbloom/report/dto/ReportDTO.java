package sams.feedbloom.report.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sams.feedbloom.common.dto.CommonDTO;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO extends CommonDTO {
	private LocalDateTime date;
	private long totalUsers;
	private long totalOrders;
	private double totalRevenue;
}