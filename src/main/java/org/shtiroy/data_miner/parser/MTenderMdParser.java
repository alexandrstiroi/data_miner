package org.shtiroy.data_miner.parser;

import org.shtiroy.data_miner.exception.ParserException;
import org.shtiroy.data_miner.model.Tender;

import java.util.List;

public class MTenderMdParser implements SiteParser{
    @Override
    public List<Tender> parse(String url) throws ParserException {
        return List.of();
    }
}
