package org.shtiroy.data_miner.parser;

public class ParserFactory {
    public static SiteParser getParser(String siteName){
        return switch (siteName) {
            case "mtender.gov.md" -> new MTenderMdParser();
            default -> throw new IllegalArgumentException("Unknown site: " + siteName);
        };
    }
}
