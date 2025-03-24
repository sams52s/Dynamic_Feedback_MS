package sams.feedbloom.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import sams.feedbloom.common.entity.CommonEntity;

import java.util.List;

@NoRepositoryBean
public interface CommonRepository<T extends CommonEntity, ID> extends JpaRepository<T, ID> {
	List<T> findByIsDeletedFalse();
}