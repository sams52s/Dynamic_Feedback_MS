package sams.feedbloom.user.repository;

import org.springframework.stereotype.Repository;
import sams.feedbloom.common.repository.CommonRepository;
import sams.feedbloom.user.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends CommonRepository<User, Long> {
	Optional<User> findByEmailIgnoreCase(String email);
}
