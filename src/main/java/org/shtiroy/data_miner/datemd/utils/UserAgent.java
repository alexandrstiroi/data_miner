package org.shtiroy.data_miner.datemd.utils;

import java.util.List;
import java.util.Random;

public enum UserAgent {
    USER_AGENT_1("agent_1","Mozilla/5.0 (Windows NT 10.0; WOW64; rv:45.0) Gecko/20100101 Firefox/45.0"),
    USER_AGENT_2("agent_2","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 YaBrowser/20.9.3.136 Yowser/2.5 Safari/537.36"),
    USER_AGENT_3("agent_3","Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/62.0"),
    USER_AGENT_4("agent_4","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.105 YaBrowser/21.3.3.230 Yowser/2.5 Safari/537.36"),
    USER_AGENT_5("agent_5","newspaper/0.2.8"),
    USER_AGENT_6("agent_6","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.141 Safari/537.36"),
    USER_AGENT_7("agent_7","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36"),
    USER_AGENT_8("agent_8","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:81.0) Gecko/20100101 Firefox/81.0"),
    USER_AGENT_9("agent_9","Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:90.0) Gecko/20100101 Firefox/90.0"),
    USER_AGENT_10("agent_10","Twitterbot/1.0"),
    USER_AGENT_11("agent_11","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.18 Safari/537.36 OPR/55.0.2962.0 (Edition developer)"),
    USER_AGENT_12("agent_12","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.152 YaBrowser/21.2.2.102 Yowser/2.5 Safari/537.36"),
    USER_AGENT_13("agent_13","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.135 YaBrowser/21.6.2.855 Yowser/2.5 Safari/537.36");

    private String type;
    private String description;

    UserAgent(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    private static final List<UserAgent> VALUES = List.of(values());
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    public static UserAgent randomUserAgent()  {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
