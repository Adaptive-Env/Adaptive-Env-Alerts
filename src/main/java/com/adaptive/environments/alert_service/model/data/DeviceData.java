package com.adaptive.environments.alert_service.model.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceData {
    private String deviceId;
    private String sensorId;
    private String location;
    private String type;
    private Long timestamp;
    private String authKey;
    private Map<String, Object> data;

    @Override
    public String toString() {
        return "DeviceData{" +
                "deviceId='" + deviceId + '\'' +
                ", sensorId='" + sensorId + '\'' +
                ", location='" + location + '\'' +
                ", type='" + type + '\'' +
                ", timestamp=" + timestamp +
                ", authKey='" + authKey + '\'' +
                ", data=" + data +
                '}';
    }
}