package sams.feedbloom.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import sams.feedbloom.common.entity.Common;

@NoRepositoryBean
public interface CommonRepository<T extends Common, ID> extends JpaRepository<T, ID> {
}