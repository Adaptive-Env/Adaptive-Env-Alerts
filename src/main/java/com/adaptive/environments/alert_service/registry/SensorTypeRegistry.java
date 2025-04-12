package com.adaptive.environments.alert_service.registry;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SensorTypeRegistry {

    private final Map<String, Map<String, Class<?>>> schema = new HashMap<>();

    public SensorTypeRegistry() {
        schema.put("fluid_temp", Map.of("value", Double.class, "unit", String.class));
        schema.put("pressure_out", Map.of("value", Double.class, "unit", String.class));
        schema.put("rpm", Map.of("value", Double.class, "unit", String.class));
    }

    public Map<String, Class<?>> getSchemaFor(String sensorType) {
        return schema.get(sensorType);
    }

    public boolean supports(String sensorType) {
        return schema.containsKey(sensorType);
    }
}
