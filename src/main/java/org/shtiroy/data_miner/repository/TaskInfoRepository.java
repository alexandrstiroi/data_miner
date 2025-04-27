package org.shtiroy.data_miner.repository;

import org.shtiroy.data_miner.entity.TaskInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskInfoRepository extends JpaRepository<TaskInfo, Integer> {
}
