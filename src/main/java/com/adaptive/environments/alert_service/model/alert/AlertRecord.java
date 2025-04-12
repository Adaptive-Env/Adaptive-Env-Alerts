package com.adaptive.environments.alert_service.model.alert;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AlertRecord {
    private String deviceId;
    private AlertType type;
    private AlertSeverity severity;
    private Long timestamp;
    private String description;
}
