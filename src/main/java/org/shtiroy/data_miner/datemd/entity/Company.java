package org.shtiroy.data_miner.datemd.entity;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(schema = "working_data", name = "t_company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer companyId;
    @Column(name = "idno")
    private String idno;
    @Column(name = "date_start")
    private Date dateStart;
    @Column(name = "company_name")
    private String companyName;
    @Column(name = "company_type")
    private String companyType;
    @Column(name = "company_address")
    private String companyAddress;
    @Column(name = "cuatm")
    private String cuatm;
    @Column(name = "company_leaders")
    private String companyLeaders;
    @Column(name = "company_founders")
    private String companyFounders;
    @Column(name = "company_beneficiaries")
    private String companyBeneficiaries;
    @Column(name = "company_activities")
    private String companyActivities;
    @Column(name = "company_activities_lic")
    private String companyActivitiesLic;
    @Column(name = "date_end")
    private Date dateEnd;

    public Company() {
    }

    public Company(String idno, Date dateStart, String companyName, String companyType, String companyAddress,
                   String cuatm, String companyLeaders, String companyFounders, String companyBeneficiaries,
                   String companyActivities, String companyActivitiesLic, Date dateEnd) {
        this.idno = idno;
        this.dateStart = dateStart;
        this.companyName = companyName;
        this.companyType = companyType;
        this.companyAddress = companyAddress;
        this.cuatm = cuatm;
        this.companyLeaders = companyLeaders;
        this.companyFounders = companyFounders;
        this.companyBeneficiaries = companyBeneficiaries;
        this.companyActivities = companyActivities;
        this.companyActivitiesLic = companyActivitiesLic;
        this.dateEnd = dateEnd;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }

    public String getCuatm() {
        return cuatm;
    }

    public void setCuatm(String cuatm) {
        this.cuatm = cuatm;
    }

    public String getCompanyLeaders() {
        return companyLeaders;
    }

    public void setCompanyLeaders(String companyLeaders) {
        this.companyLeaders = companyLeaders;
    }

    public String getCompanyFounders() {
        return companyFounders;
    }

    public void setCompanyFounders(String companyFounders) {
        this.companyFounders = companyFounders;
    }

    public String getCompanyBeneficiaries() {
        return companyBeneficiaries;
    }

    public void setCompanyBeneficiaries(String companyBeneficiaries) {
        this.companyBeneficiaries = companyBeneficiaries;
    }

    public String getCompanyActivities() {
        return companyActivities;
    }

    public void setCompanyActivities(String companyActivities) {
        this.companyActivities = companyActivities;
    }

    public String getCompanyActivitiesLic() {
        return companyActivitiesLic;
    }

    public void setCompanyActivitiesLic(String companyActivitiesLic) {
        this.companyActivitiesLic = companyActivitiesLic;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return idno.equals(company.idno);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idno);
    }

    @Override
    public String toString() {
        return  "idno=" + idno + ", companyName=" + companyName;
    }
}
