package org.shtiroy.data_miner.model;

public class Tender {
    private String name;
    private String url;
    private String customerName;
    private String value;
    private String date;

    public Tender() {
    }

    public Tender(String name, String url, String customerName, String value, String date) {
        this.name = name;
        this.url = url;
        this.customerName = customerName;
        this.value = value;
        this.date = date;
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
}
