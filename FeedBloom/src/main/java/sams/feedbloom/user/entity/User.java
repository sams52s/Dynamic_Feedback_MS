package sams.feedbloom.user.entity;

import jakarta.persistence.*;
import lombok.Data;
import sams.feedbloom.common.entity.Common;

@Data
@Entity
@Table(name = "users", indexes = {
		@Index(name = "idx_user_email", columnList = "email")
})
public class User extends Common {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false, unique = true)
	private String email;
	
	@Column(nullable = false)
	private String password;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserRole role = UserRole.USER; // Default role
}