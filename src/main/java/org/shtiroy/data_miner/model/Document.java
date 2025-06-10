package org.shtiroy.data_miner.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Document {
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime datePublished;
    private String description;
    private String documentType;
    private String title;
    private String url;
}
