package org.shtiroy.data_miner.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.shtiroy.data_miner.config.JacksonConfig;
import org.shtiroy.data_miner.model.Document;
import org.shtiroy.data_miner.model.Period;
import org.shtiroy.data_miner.model.Lot;
import org.shtiroy.data_miner.model.TenderDetail;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Entity
@Table(schema = "public", name = "tender_detail")
@Data
public class TenderDetailDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String uniqueId;
    private String urls;
    private String category;
    private String categoryName;
    private BigDecimal amount;
    private String currency;
    private String lots;
    private String status;
    private String statusDetails;
    private String period;
    private LocalDateTime auctionPeriod;
    private String documents;

    public TenderDetail toModel(){
        TenderDetail result = new TenderDetail();
        result.setId(this.id);
        result.setName(this.name);
        result.setUniqueId(this.uniqueId);
        result.setUrls(this.urls);
        result.setCategory(this.category);
        result.setCategoryName(this.categoryName);
        result.setAmount(this.amount);
        result.setCurrency(this.currency);
        result.setStatus(this.status);
        result.setStatusDetails(this.statusDetails);
        result.setAuctionPeriod(this.auctionPeriod);
        try{
            ObjectMapper mapper = JacksonConfig.getObjectMapper();
            List<Lot> list = mapper.readValue(this.lots, mapper.getTypeFactory().constructCollectionType(List.class, Lot.class));
            Period enquiry = mapper.readValue(this.period, Period.class);
            List<Document> documentList = mapper.readValue(this.documents, mapper.getTypeFactory().constructCollectionType(List.class, Document.class));
            result.setLots(list);
            result.setPeriod(enquiry);
            result.setDocuments(documentList);
        } catch (JsonProcessingException exception){
            log.error("ошибка парсенга json {}", exception.getMessage());
        }
        return result;
    }
}