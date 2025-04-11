package org.shtiroy.data_miner.util;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.shtiroy.data_miner.model.Tender;
import org.shtiroy.data_miner.model.TenderDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class HttpUtil {

    private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);

    public static List<Tender> getHttpItemsJson(String url) {
        try {
            log.info("parse url - {}", url);
            Document doc = Jsoup.connect(url).get();

            Elements elements = doc.select("div");
            List<Element> list = new ArrayList<>();
            for (Element element : elements) {
                if (element.attribute("class") != null) {
                    if (element.attribute("class").getValue().equals("tender__list__item")) {
                        list.add(element);
                    }
                }
            }
            return list.stream()
                    .map(HttpUtil::getTender)
                    .toList();
        } catch (IOException exception) {
            log.error("JSOUP {}", exception.getMessage());
        }
        return null;
    }


    private static Tender getTender(Element element) {
        Tender tender = new Tender();
        Element title = element.getElementsByClass("tender__list__item__title").first();
        Elements urlList = title != null ? title.select("a") : null;
        tender.setName(urlList != null ? Objects.requireNonNull(urlList.first()).text() : null);
        tender.setUrl("https://achizitii.md" + (urlList != null ? Objects.requireNonNull(urlList.first()).attr("href") : null));
        Element customerName = element.getElementsByClass("tender__list__item__client__name").first();
        tender.setCustomerName(customerName != null ? customerName.text() : null);
        tender.setValue(element.getElementsByClass("tender__list__item__price").text());
        tender.setDate(element.getElementsByClass("tender__list__item__info__date").text());
        return tender;
    }
}
