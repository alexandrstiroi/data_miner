package org.shtiroy.data_miner.model;

import java.time.LocalDateTime;

public class Tender {
    private String name;
    private String url;
    private String customerName;
    private String value;
    private String date;
    private String customerId;
    private LocalDateTime createdAt;

    public Tender() {
    }

    public Tender(String name, String url, String customerName, String value, String date, String customerId,
                  LocalDateTime createdAt) {
        this.name = name;
        this.url = url;
        this.customerName = customerName;
        this.value = value;
        this.date = date;
        this.customerId = customerId;
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
