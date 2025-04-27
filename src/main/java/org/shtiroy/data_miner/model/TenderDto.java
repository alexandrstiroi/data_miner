package org.shtiroy.data_miner.model;

import lombok.Data;

@Data
public class TenderDto {
    private Integer id;
    private String name;
    private String url;
    private String customerName;
    private String customerId;
    private String value;
    private String date;
    private String uniqueId;
    private String category;
}
