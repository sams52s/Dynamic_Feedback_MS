package sams.feedbloom.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import sams.feedbloom.common.entity.Common;

import java.util.List;

@NoRepositoryBean
public interface CommonRepository<T extends Common, ID> extends JpaRepository<T, ID> {
	List<T> findByIsDeletedFalse();
}