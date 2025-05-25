package org.shtiroy.data_miner.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "t_customer_analysis")
public class CustomerAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String customerId;
    private String customerResult;

    public CustomerAnalysis() {
    }

    public CustomerAnalysis(Integer id, String customerId, String customerResult) {
        this.id = id;
        this.customerId = customerId;
        this.customerResult = customerResult;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerResult() {
        return customerResult;
    }

    public void setCustomerResult(String customerResult) {
        this.customerResult = customerResult;
    }
}
