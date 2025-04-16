package com.adaptive.environments.alert_service.model.data;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidatedData {
    private DeviceData deviceData;
    private String hash;

    @Override
    public String toString() {
        return "ValidatedData{" +
                "deviceData=" + deviceData +
                ", hash='" + hash + '\'' +
                '}';
    }
}
