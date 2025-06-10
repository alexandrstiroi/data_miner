package org.shtiroy.data_miner.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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

    public static LocalDateTime ISO_DATE_TIMEToDateTime(String value){
        try {
            // 1. Парсим строку как Instant (учитывая 'Z' как UTC)
            Instant instant = Instant.parse(value);

            // 2. Конвертируем в нужную временную зону
            ZonedDateTime chisinauTime = instant.atZone(ZoneId.of("Europe/Chisinau"));

            // 3. Преобразуем в LocalDateTime (без информации о зоне)
            return chisinauTime.toLocalDateTime();
        } catch (Exception ex) {
            log.error("Ошибка {}, парсинг даты {}", ex.getMessage(), value);
            return null;
        }
    }

    public static String dateTimeToStr(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return localDateTime.format(formatter);
    }
}
