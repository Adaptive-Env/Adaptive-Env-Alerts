package com.adaptive.environments.alert_service.model.alert;


import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AlertCondition {
    private String deviceType;
    private String parameter;
    private AlertSeverity severity;
    private ComparisonOperator operator;
    private String value;
    private String description;
}