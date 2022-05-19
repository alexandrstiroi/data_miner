package org.shtiroy.data_miner.datemd.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Component
public class UrlParse {
    private final static Logger LOGGER = LogManager.getLogger(UrlParse.class.getName());
    /**
     * Вызов метода GET.
     * @param urlStr - адрес.
     * @return - JSON объект.
     */
    public Object methodGet(String urlStr){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("user-agent", UserAgent.randomUserAgent().getDescription());
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
        try {
            ResponseEntity<String> response
                    = restTemplate.getForEntity(urlStr, String.class, entity);
            return response.getBody();
        } catch (HttpServerErrorException exception) {
            LOGGER.error("Error - " + urlStr + " - " + exception.getMessage());
        }
        return null;
    }
}
