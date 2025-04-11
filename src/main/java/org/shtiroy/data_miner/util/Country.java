package org.shtiroy.data_miner.util;

public enum Country {
    MOLDOVA("moldova", "MOLDOVA");

    private final String name;
    private final String upperName;

    Country(String name, String upperName){
        this.name = name;
        this.upperName = upperName;
    }


    public String getName() {
        return name;
    }

    public String getUpperName() {
        return upperName;
    }
}
