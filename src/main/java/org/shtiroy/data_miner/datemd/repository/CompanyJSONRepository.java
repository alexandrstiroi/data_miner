package org.shtiroy.data_miner.datemd.repository;

import org.shtiroy.data_miner.datemd.entity.CompanyJSON;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Repository
public interface CompanyJSONRepository extends JpaRepository<CompanyJSON, Integer> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "INSERT INTO working_data.t_company_json(idno, create_ts, company_data) " +
            "values(?1, ?2, to_json(?3))",
        nativeQuery = true)
    void saveCompany(String idno, Timestamp create_ts, String company_data);
}
