package sams.feedbloom.authentication.repository;

import org.springframework.stereotype.Repository;
import sams.feedbloom.authentication.entity.User;
import sams.feedbloom.common.repository.CommonRepository;

import java.util.Optional;

@Repository
public interface UserRepository extends CommonRepository<User, Long> {
	Optional<User> findByEmailIgnoreCase(String email);
}
