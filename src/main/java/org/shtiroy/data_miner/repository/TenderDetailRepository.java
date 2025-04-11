package org.shtiroy.data_miner.repository;

import org.shtiroy.data_miner.entity.TenderDetailDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TenderDetailRepository extends JpaRepository<TenderDetailDto, Integer> {
    List<TenderDetailDto> findByUniqueId(String uniqueId);
}
