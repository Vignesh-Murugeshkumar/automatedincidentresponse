package com.team.incidentresponse.util;

import java.util.Map;

public class ParameterInjector {

    public static void inject(Map<String, Object> parameters, Map<String, Object> context) {
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof String && ((String) value).startsWith("{{") && ((String) value).endsWith("}}")) {
                String key = ((String) value).replace("{{", "").replace("}}", "").trim();
                if (context.containsKey(key)) {
                    entry.setValue(context.get(key));
                }
            }
        }
    }
}
