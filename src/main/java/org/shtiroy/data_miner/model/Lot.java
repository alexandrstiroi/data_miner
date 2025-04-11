package org.shtiroy.data_miner.model;

import lombok.Data;

import java.util.List;

@Data
public class Lot {
    private String uuid;
    private String description;
    private String title;
    private Value value;
    private String status;
    private List<LotItem> lotItems;
    private List<LotSupplier> lotSuppliers;
}
