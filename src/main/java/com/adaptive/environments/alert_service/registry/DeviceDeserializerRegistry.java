package com.adaptive.environments.alert_service.registry;

import com.adaptive.environments.alert_service.model.data.DeviceData;
import com.adaptive.environments.alert_service.model.data.LightBulbData;
import com.adaptive.environments.alert_service.model.data.TemperatureSensorData;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DeviceDeserializerRegistry {
    private final Map<String, Class<? extends DeviceData>> registry = new HashMap<>();

    public DeviceDeserializerRegistry() {
        registry.put("temperature", TemperatureSensorData.class);
        registry.put("light", LightBulbData.class);
    }

    public Class<? extends DeviceData> resolve(String type) {
        return registry.get(type);
    }
}