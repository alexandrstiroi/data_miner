package org.shtiroy.data_miner.model;

import lombok.Data;

@Data
public class LotSupplier {
    private String id;
    private String name;
    private String status;
    private String description;
    private Value value;
}
