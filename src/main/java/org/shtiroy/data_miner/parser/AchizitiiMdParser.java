package org.shtiroy.data_miner.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shtiroy.data_miner.exception.ParserException;
import org.shtiroy.data_miner.model.Tender;
import org.shtiroy.data_miner.util.HttpUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class AchizitiiMdParser implements SiteParser{

    private final static Logger log = LogManager.getLogger(AchizitiiMdParser.class.getName());

    @Override
    public List<Tender> parse(String url) throws ParserException {
        List<Tender> list = new ArrayList<>();
        list.addAll(Optional.ofNullable(HttpUtil.getHttpItemsJson(url)).get());
        return list;
    }
}
