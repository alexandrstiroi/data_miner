package org.shtiroy.data_miner.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shtiroy.data_miner.exception.ParserException;
import org.shtiroy.data_miner.model.*;
import org.shtiroy.data_miner.util.DateUtil;
import org.shtiroy.data_miner.util.JSONValue;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MTenderDetailParser implements SiteDetailParse{

    private final static Logger log = LogManager.getLogger(MTenderDetailParser.class.getName());
    private final WebClient webClient;

    public MTenderDetailParser (WebClient webClient){
        this.webClient = webClient;
    }

    @Override
    public TenderDetail parse(String url) {
        log.info("Get Tender detail MTender");
        TenderDetail detail = new TenderDetail();
        String response = "";
        try {
            response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.debug("Response MTender  - {}", response);
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode records = mapper.readTree(response).withArrayProperty("records");
            int size = records.size();
            for (int i = 0; i < size; i++){
                JsonNode tenderInfo = records.get(i);
                String ocid = tenderInfo.get("ocid").asText();
                if (ocid.contains("-PN-")){
                    log.info("-PN- {}", ocid);
                }
                if (ocid.contains("-EV-")) {
                    log.info("-EV- {}", ocid);
                    detail.setLots(parseLots(tenderInfo));
                    detail.setStatus(getStatus(tenderInfo));
                    detail.setStatusDetails(getStatusDetail(tenderInfo));
                    detail.setPeriod(getPeriod(tenderInfo));
                    detail.setAuctionPeriod(getAuction(tenderInfo));
                    detail.setDocuments(getDocuments(tenderInfo));
                }
                if (!ocid.contains("-EV-") & !ocid.contains("-PN-") & !ocid.contains("-AC-")) {
                    log.info("!-EV- !-PN- !-AC- {}", ocid);
                    parseTenderInfo(detail, tenderInfo);
                }
            }
            detail.setUrls(url);
            detail.setTenderJson(response);
        } catch (WebClientResponseException | JsonProcessingException ex){
            log.error("Ошибка {}", ex.getMessage());
            throw new ParserException("Ошибка парсинга " + ex.getMessage());
        }
        return detail;
    }

    private void parseTenderInfo(TenderDetail detail, JsonNode json){
        detail.setUniqueId(json.get("ocid").asText());
        detail.setCategory(json.get("compiledRelease").get("tender").get("classification").get("id").asText());
        detail.setCategoryName(json.get("compiledRelease").get("tender").get("classification").get("description").asText());
        if (json.get("compiledRelease").get("tender").get("value").get("amount").isNull()){
            detail.setAmount(null);
        } else {
            detail.setAmount(BigDecimal.valueOf(json.get("compiledRelease").get("tender").get("value").get("amount").asDouble()));
        }
        detail.setCurrency(json.get("compiledRelease").get("tender").get("value").get("currency").asText());
    }

    private List<Lot> parseLots(JsonNode jsonNode){
        List<Lot> lots = new ArrayList<>();
        JsonNode tender = jsonNode.get("compiledRelease").get("tender");
        ArrayNode lotsNode = JSONValue.getArrayNode(tender.get("lots"));
        lotsNode.forEach(json -> {
            Lot lot = new Lot();
            lot.setTitle(JSONValue.getText(json.get("title")));
            lot.setUuid(JSONValue.getText(json.get("id")));
            lot.setDescription(JSONValue.getText(json.get("description")));
            lot.setStatus(JSONValue.getText(json.get("status")));
            lot.setValue(getValue(json.get("value")));
            lots.add(lot);
        });

        ArrayNode itemsNode = JSONValue.getArrayNode(tender.get("items"));
        lots.forEach(lot ->{
            List<LotItem> items = new ArrayList<>();
            itemsNode.forEach(json ->{
                if (lot.getUuid().equals(JSONValue.getText(json.get("relatedLot")))){
                    LotItem item = new LotItem();
                    item.setDescription(JSONValue.getText(json.get("description")));
                    item.setCount(JSONValue.getInteger(json.get("quantity")));
                    item.setCodeCPV(JSONValue.getText(json.get("classification").get("id")));
                    items.add(item);
                }
            });
            lot.setLotItems(items);
        });
        ArrayNode awardsNode = JSONValue.getArrayNode(jsonNode.get("compiledRelease").get("awards"));
        if ((awardsNode != null) && !awardsNode.isEmpty()) {
            lots.forEach(lot -> {
                List<LotSupplier> suppliers = new ArrayList<>();
                awardsNode.forEach(json -> {
                    if (lot.getUuid().equals(JSONValue.getArrayNode(json.get("relatedLots")).get(0).asText())) {
                        LotSupplier supplier = new LotSupplier();
                        supplier.setDescription(JSONValue.getText(json.get("description")));
                        JsonNode supplierId = JSONValue.getArrayNodeFirst(json.get("suppliers"));
                        supplier.setId(supplierId != null ? supplierId.get("id").asText() : null);
                        supplier.setName(supplierId != null ? supplierId.get("name").asText() : null);
                        supplier.setValue(getValue(json.get("value")));
                        supplier.setStatus(JSONValue.getText(json.get("statusDetails")));
                        suppliers.add(supplier);
                    }
                });
                lot.setLotSuppliers(suppliers);
            });
        }
        return lots;
    }

    private Value getValue(JsonNode json){
        if (json != null) {
            Value value = new Value();
            value.setAmount(BigDecimal.valueOf(JSONValue.getDouble(json.get("amount"))));
            value.setCurrency(JSONValue.getText(json.get("currency")));
            return value;
        }
        return null;
    }

    private String getStatus(JsonNode jsonNode){
        JsonNode tender = jsonNode.get("compiledRelease").get("tender");
        return JSONValue.getText(tender.get("status"));
    }

    private String getStatusDetail(JsonNode jsonNode){
        JsonNode tender = jsonNode.get("compiledRelease").get("tender");
        return JSONValue.getText(tender.get("statusDetails"));
    }

    private Period getPeriod(JsonNode jsonNode){
        JsonNode tender = jsonNode.get("compiledRelease").get("tender");
        Period result = new Period();
        EnquiryPeriod enquiryPeriod = new EnquiryPeriod();
        TenderPeriod tenderPeriod = new TenderPeriod();
        if (tender.get("enquiryPeriod") != null){
            if (tender.get("enquiryPeriod").get("startDate") != null){
                enquiryPeriod.setStartDate(DateUtil.ISO_DATE_TIMEToDateTime(
                        JSONValue.getText(tender.get("enquiryPeriod").get("startDate"))));
            }
            if (tender.get("enquiryPeriod").get("endDate") != null){
                enquiryPeriod.setEndDate(DateUtil.ISO_DATE_TIMEToDateTime(
                        JSONValue.getText(tender.get("enquiryPeriod").get("endDate"))));
            }
        }
        if (tender.get("tenderPeriod") != null){
            if (tender.get("tenderPeriod").get("startDate") != null){
                tenderPeriod.setStartDate(DateUtil.ISO_DATE_TIMEToDateTime(
                        JSONValue.getText(tender.get("tenderPeriod").get("startDate"))));
            }
            if (tender.get("tenderPeriod").get("endDate") != null){
                tenderPeriod.setEndDate(DateUtil.ISO_DATE_TIMEToDateTime(
                        JSONValue.getText(tender.get("tenderPeriod").get("endDate"))));
            }
        }
        result.setEnquiryPeriod(enquiryPeriod);
        result.setTenderPeriod(tenderPeriod);
        ArrayNode awardsNode = JSONValue.getArrayNode( tender.get("enquiries"));
        if (awardsNode!= null && !awardsNode.isEmpty()) {
            List<Enquiry> enquiries = new ArrayList<>();
            awardsNode.forEach(enquiry -> {
                Enquiry en = new Enquiry();
                en.setDate(DateUtil.ISO_DATE_TIMEToDateTime(JSONValue.getText(enquiry.get("date"))));
                en.setDateAnswered(DateUtil.ISO_DATE_TIMEToDateTime(JSONValue.getText(enquiry.get("dateAnswered"))));
                en.setTitle(JSONValue.getText(enquiry.get("title")));
                en.setDescription(JSONValue.getText(enquiry.get("description")));
                en.setAnswer(JSONValue.getText(enquiry.get("answer")));
                enquiries.add(en);
            });
            result.setEnquiries(enquiries);
        }
        return result;
    }

    private LocalDateTime getAuction(JsonNode jsonNode){
        JsonNode tender = jsonNode.get("compiledRelease").get("tender");
        if (tender.has("auctionPeriod")){
            return DateUtil.ISO_DATE_TIMEToDateTime(JSONValue.getText(tender.get("auctionPeriod").get("startDate")));
        }
        return null;
    }

    private List<Document> getDocuments(JsonNode jsonNode){
        JsonNode tender = jsonNode.get("compiledRelease").get("tender");
        ArrayNode documents = JSONValue.getArrayNode(tender.get("documents"));
        if (documents != null && !documents.isEmpty()){
            List<Document> result = new ArrayList<>();
            documents.forEach(document -> {
                Document doc = new Document();
                doc.setDatePublished(DateUtil.ISO_DATE_TIMEToDateTime(JSONValue.getText(document.get("datePublished"))));
                doc.setTitle(JSONValue.getText(document.get("title")));
                doc.setDescription(JSONValue.getText(document.get("description")));
                doc.setDocumentType(JSONValue.getText(document.get("documentType")));
                doc.setUrl(JSONValue.getText(document.get("url")));
                result.add(doc);
            });
            return result;
        }
        return null;
    }
}
