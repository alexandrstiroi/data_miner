package org.shtiroy.data_miner.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
    private static final Logger log = LogManager.getLogger(DateUtil.class.getName());

    public static LocalDateTime strToDateTime(String value){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        try {
            return LocalDateTime.parse(value, formatter);
        } catch (Exception ex){
            log.error("Ошибка {}, парсинг даты {}",ex.getMessage(), value);
            return null;
        }
    }
}
