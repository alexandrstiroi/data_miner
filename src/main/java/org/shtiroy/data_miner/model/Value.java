package org.shtiroy.data_miner.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Value {
    private BigDecimal amount;
    private String currency;
}
