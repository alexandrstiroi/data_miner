package org.shtiroy.data_miner.util;

import lombok.Getter;

@Getter
public enum TaskStatusEnum {
    START("START","старт"),
    DOWNLOAD_INFO("DOWNLOAD","Скачена информация"),
    ERROR("ERROR", "Ошибка при работе таски"),
    DONE("DONE","задача выполнена");

    private final String value;
    private final String comment;

    TaskStatusEnum(String value, String comment){
        this.value = value;
        this.comment = comment;
    }
}