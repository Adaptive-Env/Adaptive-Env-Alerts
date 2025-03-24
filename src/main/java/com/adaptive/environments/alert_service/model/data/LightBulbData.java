package com.adaptive.environments.alert_service.model.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LightBulbData implements DeviceData {
    private String deviceId;
    private String authKey;
    private String type;
    private Long timestamp;
    private Boolean state;
    private Integer brightness;
}
