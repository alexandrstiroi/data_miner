package org.shtiroy.data_miner.parser;

import org.shtiroy.data_miner.model.TenderDetail;

public interface SiteDetailParse {
    TenderDetail parse(String url);
}
