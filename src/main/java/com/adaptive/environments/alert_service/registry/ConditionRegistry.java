package com.adaptive.environments.alert_service.registry;

import com.adaptive.environments.alert_service.model.alert.AlertCondition;
import com.adaptive.environments.alert_service.model.alert.AlertSeverity;
import com.adaptive.environments.alert_service.model.alert.ComparisonOperator;
import com.adaptive.environments.alert_service.service.AlertConditionValidator;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ConditionRegistry {
    /// In current version of the system there is only one permitted condition per parameter
    private final Map<String, AlertCondition> conditions = new ConcurrentHashMap<>();
    private final AlertConditionValidator alertConditionValidator;

    public ConditionRegistry(AlertConditionValidator alertConditionValidator) {
        this.alertConditionValidator = alertConditionValidator;
        conditions.put("temperature", new AlertCondition("temperature", "temperature",
                AlertSeverity.WARNING, ComparisonOperator.GREATER_THAN, "70",
                "Temperature is too high!"));
        conditions.put("light", new AlertCondition("light", "state", AlertSeverity.CRITICAL,
                ComparisonOperator.EQUALS, "ON",
                "Light switch should be on!"));
    }

    public AlertCondition getCondition(String parameter) {
        return conditions.get(parameter);
    }

    public void setCondition(AlertCondition condition) {
        if (alertConditionValidator.validate(condition)) {
            String conditionID = condition.getDeviceType() + "-" + condition.getParameter();
            conditions.put(conditionID, condition);
        }
    }

    public Map<String, AlertCondition> getAllConditions() {
        return Map.copyOf(conditions);
    }

}