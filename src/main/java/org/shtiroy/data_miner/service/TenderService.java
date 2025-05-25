package org.shtiroy.data_miner.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shtiroy.data_miner.entity.TenderDetailDto;
import org.shtiroy.data_miner.entity.TenderInfo;
import org.shtiroy.data_miner.exception.ResourceNotFoundException;
import org.shtiroy.data_miner.model.Tender;
import org.shtiroy.data_miner.model.TenderDetail;
import org.shtiroy.data_miner.model.TenderDto;
import org.shtiroy.data_miner.parser.AchizitiiMDDetailParser;
import org.shtiroy.data_miner.parser.MTenderDetailParser;
import org.shtiroy.data_miner.repository.TenderDetailRepository;
import org.shtiroy.data_miner.repository.TenderRepository;
import org.shtiroy.data_miner.util.Country;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TenderService {
    private final TenderRepository tenderRepository;
    private final TenderDetailRepository tenderDetailRepository;
    private final AchizitiiMDDetailParser parser;
    private final MTenderDetailParser mParser;

    private final static Logger log = LogManager.getLogger(TenderService.class.getName());

    public TenderService(TenderRepository tenderRepository, TenderDetailRepository tenderDetailRepository, AchizitiiMDDetailParser parser, MTenderDetailParser mParser){
        this.tenderRepository = tenderRepository;
        this.tenderDetailRepository = tenderDetailRepository;
        this.parser = parser;
        this.mParser = mParser;
    }

    public boolean saveAchizitii(Tender tender){
        log.info("Обработка тендера {}", tender.getCustomerName());
        TenderInfo info = getTenderFromAchizitii(tender);
        try {
            if (tenderRepository.findByNameAndUrl(info.getName(), tender.getUrl()).isEmpty()) {
                tenderRepository.save(info);
            } else {
                tenderRepository.updateTender(info.getValue(), info.getUrl(), info.getName());
            }
        } catch (Exception ex){
            log.error("ERROR update or save element {}", info);
            log.error("ERROR detail {}", ex.getMessage());
            return false;
        }
        return true;
    }

    public TenderDetail getTenderDetail(Integer tenderId){
        TenderInfo tender = tenderRepository.findById(tenderId).orElseThrow(() ->
                new ResourceNotFoundException("Тендер по id=" + tenderId + " не найден"));
        TenderDetail detail = null;
        if (tender.getUniqueId() == null){
            detail = parser.parse(tender.getUrl());
            tender.setUniqueId(detail.getUniqueId());
            tender.setDate(detail.getDate());
            tender.setCustomerId(detail.getCostumerId());
            detail = mParser.parse("https://public.mtender.gov.md/tenders/"+detail.getUniqueId());
            tenderDetailRepository.save(detail.toDto());
            tenderRepository.save(tender);
        } else {
            detail = tenderDetailRepository.findByUniqueId(tender.getUniqueId()).stream()
                    .findFirst().map(TenderDetailDto::toModel).orElse(null);
            if (detail != null){
                detail.setName(tender.getName());
                detail.setCostumerId(tender.getCustomerId());
                detail.setDate(tender.getDate());
            }
        }
        return detail;
    }

    public TenderDetail updateTenderDetail(Integer tenderId){
        TenderInfo tender = tenderRepository.findById(tenderId).orElseThrow(() ->
                new ResourceNotFoundException("Тендер по id=" + tenderId + " не найден"));
        TenderDetail detail = null;
        detail = parser.parse(tender.getUrl());
        tender.setDate(detail.getDate());
        detail = mParser.parse("https://public.mtender.gov.md/tenders/"+detail.getUniqueId());
        detail.setName(tender.getName());
        detail.setCostumerId(tender.getCustomerId());
        detail.setDate(tender.getDate());
        detail.setId(tenderDetailRepository.findByUniqueId(detail.getUniqueId()).stream().map(TenderDetailDto::getId).findFirst().orElse(null));
        tenderDetailRepository.save(detail.toDto());
        tenderRepository.save(tender);
        return detail;
    }

    public List<TenderDto> getNew(LocalDateTime dateTime){
        try{
            return tenderRepository.findByCreatedAtAfter(dateTime)
                    .stream()
                    .map(this::process)
                    .collect(Collectors.toList());
        } catch (Exception ex){
            log.error("Не смогли достать новые тендоры");
            return Collections.emptyList();
        }
    }

    private TenderInfo getTenderFromAchizitii(Tender tender){
        TenderInfo info = new TenderInfo();
        info.setCountry(Country.MOLDOVA.getUpperName());
        info.setSite("https://achizitii.md/");
        if (tender != null) {
            info.setName(tender.getName());
            info.setUrl(tender.getUrl());
            info.setCustomerName(tender.getCustomerName());
            info.setValue(tender.getValue());
            info.setDate(tender.getDate());
            if (tender.getCreatedAt() != null) {
                info.setCreatedAt(tender.getCreatedAt());
            }
        }
        return info;
    }

    private TenderDto process(TenderInfo tenderInfo){
        TenderDto result = new TenderDto();
        result.setId(tenderInfo.getId());
        result.setName(tenderInfo.getName());
        result.setUrl(tenderInfo.getUrl());
        result.setCustomerName(tenderInfo.getCustomerName());
        result.setCustomerId(tenderInfo.getCustomerId());
        result.setValue(tenderInfo.getValue());
        result.setDate(tenderInfo.getDate());
        result.setUniqueId(tenderInfo.getUniqueId());
        result.setCategory(tenderDetailRepository.findByUniqueId(tenderInfo.getUniqueId()).stream()
                .map(TenderDetailDto::getCategory)
                .findFirst().orElse(""));
        return result;
    }
}
