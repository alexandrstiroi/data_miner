package org.shtiroy.data_miner.service.schedule;

import lombok.extern.slf4j.Slf4j;
import org.shtiroy.data_miner.entity.TenderInfo;
import org.shtiroy.data_miner.parser.AchizitiiMdParser;
import org.shtiroy.data_miner.repository.TenderRepository;
import org.shtiroy.data_miner.service.TenderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ScheduledTasks {

    private final Integer ACHIZITII_PAGE_COUNT = 2666;
    private final Integer ACHIZITII_UPDATE_PAGE_COUNT = 3;
    private final AchizitiiMdParser achizitiiMdParser;
    private final TenderRepository tenderRepository;
    private final TenderService tenderService;

    public ScheduledTasks(AchizitiiMdParser achizitiiMdParser, TenderRepository tenderRepository, TenderService tenderService){
        this.achizitiiMdParser = achizitiiMdParser;
        this.tenderRepository = tenderRepository;
        this.tenderService = tenderService;
    }

    @Scheduled(cron = "${app.config.achizitiimd.cron}")
    public void getNewAchizitiimd(){
        achizitiiMdParser.parse("https://achizitii.md/ru/public/tender/list?page=", ACHIZITII_UPDATE_PAGE_COUNT);
    }

    @Scheduled(cron = "${app.config.achizitiimd.update}")
    public void getDetailAchizitiimd(){
        List<TenderInfo> list = tenderRepository.findByCustomerIdIsNull();
        list.stream()
                .map(TenderInfo::getId)
                .forEach(tenderService::getTenderDetail);
    }

    @Scheduled(cron = "${app.config.achizitiimd.cron}")
    public void getUpdateAchizitiimd(){
        achizitiiMdParser.parse("https://achizitii.md/ru/public/tender/list?page=", ACHIZITII_UPDATE_PAGE_COUNT);
    }
}
