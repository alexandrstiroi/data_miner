package org.shtiroy.data_miner.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shtiroy.data_miner.exception.ParserException;
import org.shtiroy.data_miner.model.Tender;
import org.shtiroy.data_miner.service.TenderService;
import org.shtiroy.data_miner.util.HttpUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class AchizitiiMdParser implements SiteParser{

    private final static Logger log = LogManager.getLogger(AchizitiiMdParser.class.getName());

    private final TenderService service;

    public AchizitiiMdParser(TenderService service){
        this.service = service;
    }

    @Override
    public List<Tender> parse(String url, int pageCount) throws ParserException {
        List<Tender> list = new ArrayList<>();
        list.addAll(Optional.of(HttpUtil.getHttpItemsJson(url + "None")).get());
        list.forEach(service::saveAchizitii);
        for (int i = 2; i<= pageCount; i++){
            List<Tender> tenders = HttpUtil.getHttpItemsJson(url + i);
            if (tenders != null) {
                tenders.forEach(service::saveAchizitii);
            }
            try{
                Thread.sleep(500);
            } catch (InterruptedException ex){
                log.error("Thread has been interrupted {}", ex.getMessage());
            }
        }
        return list;
    }
}
