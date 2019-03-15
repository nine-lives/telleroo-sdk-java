package com.telleroo;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class TellerooError {

    private Object error;

    public TellerooError() {
    }

    public TellerooError(String message) {
        this.error = message;
    }

    public String getMessage() {
        if (error instanceof Map) {
            return "Invalid request, check field errors";
        }
        return error instanceof String ? error.toString() : null;
    }

    public Map<String, List<String>> getFieldErrors() {
//        Map<String, String> map = new LinkedHashMap<>();
//        if (error instanceof Map) {
//            for (Map.Entry entry : ((Map<Object, Object>) error).entrySet()) {
//                map.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
//            }
//        }
//        return map;
        return error instanceof Map ? (Map<String, List<String>>) error : Collections.emptyMap();
    }

    public Object getError() {
        return error;
    }
}
