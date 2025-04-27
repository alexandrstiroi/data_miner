package org.shtiroy.data_miner.util;

import lombok.Getter;

@Getter
public enum TaskType {
    CUSTOMER_INFO("customer_info","Скачать данные по клиенту");

    private final String nameEn;
    private final String comment;

    TaskType(String nameEn, String comment){
        this.nameEn = nameEn;
        this.comment = comment;
    }
}