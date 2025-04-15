package org.shtiroy.data_miner.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.shtiroy.data_miner.entity.TenderDetailDto;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Data
public class TenderDetail {
    private Integer id;
    private String name;
    private String uniqueId;
    private String urls;
    private String category;
    private String categoryName;
    private BigDecimal amount;
    private String currency;
    private String date;
    private String costumerId;
    private List<Lot> lots;

    public TenderDetailDto toDto(){
        TenderDetailDto result = new TenderDetailDto();
        result.setId(this.id);
        result.setName(this.name);
        result.setUniqueId(this.uniqueId);
        result.setUrls(this.urls);
        result.setCategory(this.category);
        result.setCategoryName(this.categoryName);
        result.setAmount(this.amount);
        result.setCurrency(this.currency);
        try{
            ObjectMapper mapper = new ObjectMapper();
            result.setLots(mapper.writeValueAsString(this.lots));
        } catch (JsonProcessingException exception){
            log.error("error convert dto {}", exception.getMessage());
        }
        return result;
    }
}
