package org.shtiroy.data_miner.service.schedule;

import lombok.extern.slf4j.Slf4j;
import org.shtiroy.data_miner.entity.TenderInfo;
import org.shtiroy.data_miner.parser.AchizitiiMdParser;
import org.shtiroy.data_miner.repository.TenderRepository;
import org.shtiroy.data_miner.service.TenderService;
import org.shtiroy.data_miner.util.HttpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Component
@Slf4j
public class ScheduledTasks {

    private final Integer ACHIZITII_UPDATE_PAGE_COUNT = 1;
    private final AchizitiiMdParser achizitiiMdParser;
    private final TenderRepository tenderRepository;
    private final TenderService tenderService;
    @Value("${app.config.achizitiimd.tenderListUrl}")
    private String baseUrl;

    public ScheduledTasks(AchizitiiMdParser achizitiiMdParser, TenderRepository tenderRepository, TenderService tenderService){
        this.achizitiiMdParser = achizitiiMdParser;
        this.tenderRepository = tenderRepository;
        this.tenderService = tenderService;
    }

    @Scheduled(cron = "${app.config.achizitiimd.cron}")
    public void getNewAchizitiimd(){
        List<String> urlList = HttpUtil.extractPaginationUrls(baseUrl);
        urlList.stream().
                limit(urlList.size()>ACHIZITII_UPDATE_PAGE_COUNT ? ACHIZITII_UPDATE_PAGE_COUNT : urlList.size())
                .map(achizitiiMdParser::parse)
                .flatMap(Collection::stream)
                .peek(elem -> elem.setCreatedAt(LocalDateTime.now()))
                .forEach(tenderService::saveAchizitii);
    }

    @Scheduled(cron = "${app.config.achizitiimd.update}")
    public void getDetailAchizitiimd(){
        List<TenderInfo> list = tenderRepository.findByCustomerIdIsNull();
        list.stream()
                .map(TenderInfo::getId)
                .forEach(tenderService::getTenderDetail);
    }

    public void getUpdateAchizitiimd(){
        List<String> urlList = HttpUtil.extractPaginationUrls(baseUrl);
        urlList.stream().
                limit(urlList.size()>ACHIZITII_UPDATE_PAGE_COUNT ? ACHIZITII_UPDATE_PAGE_COUNT : urlList.size())
                .map(achizitiiMdParser::parse)
                .flatMap(Collection::stream)
                .peek(elem -> elem.setCreatedAt(LocalDateTime.now()))
                .forEach(tenderService::saveAchizitii);
        List<TenderInfo> list = tenderRepository.findByCustomerIdIsNull();
        list.stream()
                .map(TenderInfo::getId)
                .forEach(tenderService::getTenderDetail);
    }
}
