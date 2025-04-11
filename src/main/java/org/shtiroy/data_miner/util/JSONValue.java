package org.shtiroy.data_miner.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class JSONValue {

    public static String getText(JsonNode json){
        return json != null ? json.asText() : null;
    }

    public static ArrayNode getArrayNode(JsonNode json){
        return json != null ? (ArrayNode) json : null;
    }

    public static JsonNode getArrayNodeFirst(JsonNode json){
        return json != null ? ((ArrayNode)json).get(0) : null;
    }

    public static Integer getInteger(JsonNode json){
        return json != null && json.isNumber() ? json.asInt() : null;
    }

    public static Double getDouble(JsonNode json){
        return json != null && json.isDouble() ? json.asDouble() : null;
    }
}
