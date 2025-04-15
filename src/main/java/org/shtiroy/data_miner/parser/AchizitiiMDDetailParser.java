package org.shtiroy.data_miner.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.shtiroy.data_miner.model.TenderDetail;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AchizitiiMDDetailParser implements SiteDetailParse {
    private final static Logger log = LogManager.getLogger(AchizitiiMDDetailParser.class.getName());

    @Override
    public TenderDetail parse(String url) {
        log.info("Начинаем парсить страницу {}", url);
        TenderDetail detail = new TenderDetail();
        try{
            Document doc = Jsoup.connect(url).get();
            Elements elements = doc.getElementsByClass("tender__item__row");
            for (Element element : elements){
                String title = element.getElementsByClass("tender__item__row__title").first().text();
                switch (title){
                    case "MTender ID" :
                        detail.setUniqueId(element.select("a").text());
                        detail.setUrls(element.select("a").attr("href"));
                        break;
                    case "Фискальный код/IDNO" :
                        detail.setCostumerId(element.select("a").text());
                        break;
                    case "Codul fiscal/IDNO" :
                        detail.setCostumerId(element.select("a").text());
                        break;
                }
            }
            elements = doc.getElementsByClass("prog__etap");
            StringBuilder period = new StringBuilder();
            for (Element element : elements){
                period.append(element.getElementsByClass("prog__title prog__title__tender").first().text()).append("\n");
                period.append(element.getElementsByClass("prog__date__text").first().text()).append("\n");
            }
            detail.setDate(period.toString());
        } catch (IOException exception) {
            log.info("Ошибка парсинга url {}, ошибка {}", url, exception.getMessage());
        }
        return detail;
    }
}
