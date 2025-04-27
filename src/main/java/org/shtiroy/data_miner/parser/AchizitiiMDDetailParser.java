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
            detail.setDate(extractStagePeriods(elements));
        } catch (IOException exception) {
            log.info("Ошибка парсинга url {}, ошибка {}", url, exception.getMessage());
        }
        return detail;
    }

    /**
     * Извлекает все этапы тендера и их временные промежутки в читаемом виде.
     *
     * @param etapBlocks HTML с блоками этапов
     * @return Строка, где каждый этап и его даты указаны с новой строки
     */
    private static String extractStagePeriods(Elements etapBlocks) {
        StringBuilder result = new StringBuilder();
        for (Element etap : etapBlocks) {
            // Название этапа
            Element titleEl = etap.selectFirst(".prog__title");
            if (titleEl == null) continue;
            String title = titleEl.text().trim();
            // Блок с датами
            Element dateTextEl = etap.selectFirst(".prog__date__text");
            if (dateTextEl == null) continue;
            String dateText = dateTextEl.text().trim();
            if (dateText.isEmpty() || dateText.toLowerCase().contains("не будет использоваться")) {
                continue;
            }
            // Очистка лишнего
            String cleanedDate = dateText
                    .replaceAll("с\\s*", "с ")
                    .replaceAll("\\s*по\\s*", "по ")
                    .replaceAll("осталось \\d+ дней", "") // убираем "осталось N дней"
                    .trim();
            // Формируем строку для одного этапа
            result.append(title).append(": ").append(cleanedDate).append("\n");
        }
        return result.toString().trim();
    }
}
