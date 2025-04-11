package org.shtiroy.data_miner.repository;

import jakarta.transaction.Transactional;
import org.shtiroy.data_miner.entity.TenderInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TenderRepository extends JpaRepository<TenderInfo, Integer> {

    List<TenderInfo> findByNameAndUrl(String name, String url);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update tender set value = ?1, date = ?2 where url = ?3 and name = ?4", nativeQuery = true)
    void updateTender(String value,String date, String url, String name);
}
