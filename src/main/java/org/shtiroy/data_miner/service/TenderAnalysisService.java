package org.shtiroy.data_miner.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shtiroy.data_miner.entity.CustomerAnalysis;
import org.shtiroy.data_miner.entity.TaskInfo;
import org.shtiroy.data_miner.entity.TenderDetailDto;
import org.shtiroy.data_miner.model.Lot;
import org.shtiroy.data_miner.model.LotSupplier;
import org.shtiroy.data_miner.model.TenderDetail;
import org.shtiroy.data_miner.repository.CustomerAnalysisRepository;
import org.shtiroy.data_miner.repository.TenderDetailRepository;
import org.shtiroy.data_miner.repository.TenderRepository;
import org.shtiroy.data_miner.util.TaskStatusEnum;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Service
public class TenderAnalysisService {

    private final TenderRepository tenderRepository;
    private final TenderDetailRepository tenderDetailRepository;
    private final TaskInfoService taskInfoService;
    private final CustomerAnalysisRepository customerAnalysisRepository;
    private final Logger log = LogManager.getLogger(TenderAnalysisService.class.getName());

    public TenderAnalysisService(TenderRepository tenderRepository, TenderDetailRepository tenderDetailRepository,
                                 TaskInfoService taskInfoService, CustomerAnalysisRepository customerAnalysisRepository){
        this.tenderRepository = tenderRepository;
        this.tenderDetailRepository = tenderDetailRepository;
        this.taskInfoService = taskInfoService;
        this.customerAnalysisRepository = customerAnalysisRepository;
    }

    public Map<String, Map<String, Set<String>>> getParticipantsByCPV(String customerId) {
        log.info("Получение списка участников по codeCPV...");
        List<TenderDetailDto> tenders = tenderRepository.findByCustomerId(customerId).stream()
                .map(elem -> tenderDetailRepository.findByUniqueId(elem.getUniqueId()))
                .flatMap(Collection::stream)
                .toList();
        log.debug("Всего тендеров: {}", tenders.size());

        Map<String, Map<String, Set<String>>> result = new HashMap<>();

        for (TenderDetailDto tender : tenders) {
            TenderDetail tenderDetail = tender.toModel();
            if (tenderDetail.getLots() != null) {
                for (Lot lot : tenderDetail.getLots()) {
                    String cpv = lot.getLotItems().get(0).getCodeCPV();
                    if (cpv == null) continue;

                    Map<String, Set<String>> supplierMap = result.computeIfAbsent(cpv, k -> new HashMap<>());
                    if (lot.getLotSuppliers() != null) {
                        for (LotSupplier supplier : lot.getLotSuppliers()) {
                            String name = supplier.getName();
                            String status = supplier.getStatus();
                            if (name == null || status == null) continue;

                            Set<String> statuses = supplierMap.computeIfAbsent(name, k -> new HashSet<>());
                            statuses.add(status);
                        }
                    }
                }
            }
        }
        log.info("Группировка по codeCPV завершена. CPV кодов: {}", result.size());
        return result;
    }

    public String buildTextReport(TaskInfo taskInfo) {
        log.info("Формирование текстового отчёта...");
        taskInfoService.updateTaskStatus(taskInfo, TaskStatusEnum.ANALYSIS);
        StringBuilder sb = new StringBuilder("\uD83D\uDCCA Отчёт по участникам в категориях CPV:\n\n");
        Map<String, Map<String, Set<String>>> stats = getParticipantsByCPV(taskInfo.getTaskValue());
        for (var cpvEntry : stats.entrySet()) {
            sb.append("\uD83D\uDCC5 Категория CPV: ").append(cpvEntry.getKey()).append("\n");
            for (var supplierEntry : cpvEntry.getValue().entrySet()) {
                sb.append("  └ ").append(supplierEntry.getKey())
                        .append(" — статусы: ").append(String.join(", ", supplierEntry.getValue())).append("\n");
            }
            sb.append("\n");
        }
        log.info("Отчёт сформирован ({} CPV категорий)", stats.size());
        taskInfoService.updateTaskStatus(taskInfo, TaskStatusEnum.DONE);
        CustomerAnalysis customerAnalysis = new CustomerAnalysis();
        customerAnalysis.setCustomerId(taskInfo.getTaskValue());
        customerAnalysis.setCustomerResult(sb.toString());
        customerAnalysisRepository.save(customerAnalysis);
        return sb.toString();
    }
}
