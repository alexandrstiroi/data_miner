package org.shtiroy.data_miner.util;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.shtiroy.data_miner.model.Tender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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

    /**
     * Извлекает список всех абсолютных URL страниц пагинации, включая недостающие.
     *
     * @param baseUrl Базовый адрес (например: "https://example.com")
     * @return Отсортированный список абсолютных URL всех страниц
     */
    public static List<String> extractPaginationUrls(String baseUrl) {
        log.info("URL для извлечения {}", baseUrl);
        Elements pageLinks = null;
        try {
            Document doc = Jsoup.connect(baseUrl).get();
            pageLinks = doc.select("nav ul.pagination a[href]");
        } catch (IOException exception){
            log.error("JSOUP {}",exception.getMessage());
        }

        TreeMap<Integer, String> pageMap = new TreeMap<>();
        String pageUrlTemplate = null;

        for (Element link : pageLinks) {
            String text = link.text().trim();
            String href = link.absUrl("href");

            try {
                int pageNum = Integer.parseInt(text);
                pageMap.put(pageNum, href);

                // Сохраняем шаблон при первой возможности
                if (pageUrlTemplate == null && href.contains("page=")) {
                    int index = href.indexOf("page=");
                    pageUrlTemplate = href.substring(0, index + "page=".length());
                }

            } catch (NumberFormatException ignored) {
                // Пропускаем "Назад", "Вперед", и т.п.
            }
        }

        if (pageUrlTemplate == null) {
            log.error("Не удалось определить шаблон URL для генерации ссылок.");
            return List.of(baseUrl);
        }

        // Генерация недостающих страниц
        Map<Integer, String> generated = new HashMap<>();
        List<Integer> allPages = new ArrayList<>(pageMap.keySet());

        for (int i = 0; i < allPages.size() - 1; i++) {
            int current = allPages.get(i);
            int next = allPages.get(i + 1);
            if (next - current > 1) {
                for (int missing = current + 1; missing < next; missing++) {
                    String genUrl = pageUrlTemplate + missing;
                    generated.put(missing, genUrl);
                }
            }
        }

        // Объединение всех ссылок
        TreeMap<Integer, String> allCombined = new TreeMap<>();
        allCombined.putAll(pageMap);
        allCombined.putAll(generated);

        return new ArrayList<>(allCombined.values());
    }
}
