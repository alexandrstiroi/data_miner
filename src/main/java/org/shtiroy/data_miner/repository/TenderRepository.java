package org.shtiroy.data_miner.repository;

import jakarta.transaction.Transactional;
import org.shtiroy.data_miner.entity.TenderInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TenderRepository extends JpaRepository<TenderInfo, Integer> {

    List<TenderInfo> findByNameAndUrl(String name, String url);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update tender set value = ?1 where url = ?2 and name = ?3", nativeQuery = true)
    void updateTender(String value, String url, String name);

    List<TenderInfo> findByCustomerIdIsNull();

    List<TenderInfo> findByCreatedAtAfter(LocalDateTime createdAt);

    List<TenderInfo> findByCustomerId(String customerId);
}
