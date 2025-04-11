package org.shtiroy.data_miner.service.schedule;

import lombok.extern.slf4j.Slf4j;
import org.shtiroy.data_miner.parser.AchizitiiMdParser;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ScheduledTasks {

    private final Integer ACHIZITII_PAGE_COUNT = 2666;
    private final Integer ACHIZITII_UPDATE_PAGE_COUNT = 3;
    private final AchizitiiMdParser achizitiiMdParser;

    public ScheduledTasks(AchizitiiMdParser achizitiiMdParser){
        this.achizitiiMdParser = achizitiiMdParser;
    }

    @Scheduled(cron = "${app.config.achizitiimd.cron}")
    public void getAllAchizitiimd(){
        achizitiiMdParser.parse("https://achizitii.md/ru/public/tender/list?page=", ACHIZITII_PAGE_COUNT);
    }

    @Scheduled(cron = "${app.config.achizitiimd.cron}")
    public void getUpdateAchizitiimd(){
        achizitiiMdParser.parse("https://achizitii.md/ru/public/tender/list?page=", ACHIZITII_UPDATE_PAGE_COUNT);
    }
}
