package org.shtiroy.data_miner.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shtiroy.data_miner.entity.TenderDetailDto;
import org.shtiroy.data_miner.entity.TenderInfo;
import org.shtiroy.data_miner.exception.ResourceNotFoundException;
import org.shtiroy.data_miner.model.Tender;
import org.shtiroy.data_miner.model.TenderDetail;
import org.shtiroy.data_miner.parser.AchizitiiMDDetailParser;
import org.shtiroy.data_miner.parser.MTenderDetailParser;
import org.shtiroy.data_miner.repository.TenderDetailRepository;
import org.shtiroy.data_miner.repository.TenderRepository;
import org.shtiroy.data_miner.util.Country;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        boolean result = false;
        log.info("Обработка тендера {}", tender.getCustomerName());
        TenderInfo info = getTenderFromAchizitii(tender);
        try {
            if (tenderRepository.findByNameAndUrl(info.getName(), tender.getUrl()).isEmpty()) {
                tenderRepository.save(info);
            } else {
                tenderRepository.updateTender(info.getValue(), info.getDate(), info.getUrl(), info.getName());
            }
        } catch (Exception ex){
            log.error("ERROR update or save element {}", info);
            log.error("ERROR detail {}", ex.getMessage());
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
        }
        return detail;
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
        }
        return info;
    }
}
