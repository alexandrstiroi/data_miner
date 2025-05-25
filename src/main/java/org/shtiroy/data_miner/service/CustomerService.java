package org.shtiroy.data_miner.service;

import lombok.extern.slf4j.Slf4j;
import org.shtiroy.data_miner.entity.TaskInfo;
import org.shtiroy.data_miner.entity.TenderInfo;
import org.shtiroy.data_miner.model.Tender;
import org.shtiroy.data_miner.parser.AchizitiiMdParser;
import org.shtiroy.data_miner.repository.TenderRepository;
import org.shtiroy.data_miner.util.HttpUtil;
import org.shtiroy.data_miner.util.TaskStatusEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CustomerService {
    private final AchizitiiMdParser achizitiiMdParser;
    private final TenderService tenderService;
    private final TenderRepository tenderRepository;
    private final TaskInfoService taskInfoService;
    private final TenderAnalysisService tenderAnalysisService;

    @Value("${app.config.achizitiimd.customerTenderList}")
    private String baseUrl;

    public CustomerService(AchizitiiMdParser achizitiiMdParser, TenderService tenderService,
                           TenderRepository tenderRepository, TaskInfoService taskInfoService,
                           TenderAnalysisService tenderAnalysisService){
        this.achizitiiMdParser = achizitiiMdParser;
        this.tenderRepository = tenderRepository;
        this.tenderService = tenderService;
        this.taskInfoService = taskInfoService;
        this.tenderAnalysisService = tenderAnalysisService;
    }

    public void start(TaskInfo taskInfo){
        try {
            List<String> urlList = HttpUtil.extractPaginationUrls(baseUrl + taskInfo.getTaskValue());
            log.info("Получено {} страниц для обработки", urlList.size());

            List<Tender> listTender = new ArrayList<>();
            for (String url : urlList) {
                try {
                    List<Tender> parsed = achizitiiMdParser.parse(url);
                    parsed.forEach(elem -> elem.setCreatedAt(LocalDateTime.of(2025, 4, 19, 10, 30, 0)));
                    listTender.addAll(parsed);
                    log.debug("Успешно распарсили {} тендеров с {}", parsed.size(), url);
                } catch (Exception e) {
                    log.error("Ошибка при парсинге URL: {}", url, e);
                }
            }
            for (Tender tender : listTender) {
                try {
                    tenderService.saveAchizitii(tender);
                } catch (Exception e) {
                    log.error("Ошибка при сохранении тендера: {}", tender.getUrl(), e);
                }
            }
            log.info("Сохранили {} тендеров", listTender.size());
            List<TenderInfo> list = tenderRepository.findByCustomerIdIsNull();
            log.info("Найдено {} тендеров без customerId для детализации", list.size());

            for (TenderInfo tenderInfo : list) {
                try {
                    tenderService.getTenderDetail(tenderInfo.getId());
                } catch (Exception e) {
                    log.error("Ошибка при получении деталей тендера: {}", tenderInfo.getId(), e);
                }
            }
            log.info("Обработка заказчика {} завершена успешно", taskInfo.getTaskValue());
            taskInfoService.updateTaskStatus(taskInfo, TaskStatusEnum.DOWNLOAD_INFO);

            new Thread(() -> tenderAnalysisService.buildTextReport(taskInfo)).start();
        } catch (Exception e){
            log.error("Фатальная ошибка при обработке заказчика {}", taskInfo.getTaskValue(), e);
            taskInfoService.errorTask(taskInfo);
        }
    }
}
