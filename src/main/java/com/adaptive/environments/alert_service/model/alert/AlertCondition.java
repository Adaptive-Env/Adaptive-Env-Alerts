package com.adaptive.environments.alert_service.model.alert;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "alert_conditions")
public class AlertCondition {
    @Id
    private String id;
    private String deviceType;
    private String parameter;
    private AlertSeverity severity;
    private ComparisonOperator operator;
    private String value;
    private String description;
}