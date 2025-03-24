package sams.feedbloom.project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sams.feedbloom.common.entity.CommonEntity;
import sams.feedbloom.feedback.entity.Feedback;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(exclude = "feedbackList")
@Entity
@Table(name = "projects")
public class Project extends CommonEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<Feedback> feedbackList;
}

