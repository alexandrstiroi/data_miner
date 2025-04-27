package org.shtiroy.data_miner.parser;

import org.shtiroy.data_miner.exception.ParserException;
import org.shtiroy.data_miner.model.Tender;

import java.util.List;

public interface SiteParser {
    List<Tender> parse(String url) throws ParserException;
}
