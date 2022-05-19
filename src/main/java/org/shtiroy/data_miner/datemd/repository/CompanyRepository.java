package org.shtiroy.data_miner.datemd.repository;

import org.shtiroy.data_miner.datemd.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    @Query(value = " select t1.* " +
            " from working_data.t_company t1" +
            " where company_type = 'Societate cu răspundere limitată' " +
            " and LENGTH(t1.idno) = 13" +
            " and t1.date_end is null " +
            " and not exists(select 1 from working_data.t_company_json t2 where t2.idno = t1.idno)" +
            " fetch first ?1 rows only",
            nativeQuery = true)
    List<Company> findAllActiveCompany(Integer limit);
}
