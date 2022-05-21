package org.shtiroy.data_miner.datemd.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(schema = "working_data", name = "t_company_json")
public class CompanyJSON {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer companyJsonId;
    @Column(name = "idno")
    private String idno;
    @Column(name = "create_ts")
    private Timestamp createTs;
    @Column(name = "company_data")
    private String companyData;
    @Column(name = "resource")
    private String resource;

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public CompanyJSON() {
    }

    public CompanyJSON(String idno, Timestamp createTs, String companyData, String resource) {
        this.idno = idno;
        this.createTs = createTs;
        this.companyData = companyData;
        this.resource = resource;
    }

    public Integer getCompanyJsonId() {
        return companyJsonId;
    }

    public void setCompanyJsonId(Integer companyJsonId) {
        this.companyJsonId = companyJsonId;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public Timestamp getCreateTs() {
        return createTs;
    }

    public void setCreateTs(Timestamp createTs) {
        this.createTs = createTs;
    }

    public Object getCompanyData() {
        return companyData;
    }

    public void setCompanyData(String companyData) {
        this.companyData = companyData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyJSON that = (CompanyJSON) o;
        return idno.equals(that.idno);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idno);
    }
}
