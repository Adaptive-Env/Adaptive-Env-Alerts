package com.adaptive.environments.alert_service.service;

import com.adaptive.environments.alert_service.model.alert.AlertCondition;
import com.adaptive.environments.alert_service.model.alert.AlertRecord;
import com.adaptive.environments.alert_service.model.alert.AlertType;
import com.adaptive.environments.alert_service.model.alert.ComparisonOperator;
import com.adaptive.environments.alert_service.model.data.DeviceData;
import com.adaptive.environments.alert_service.registry.ConditionRegistry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
@Component
public class AlertRuleEngine {

    private final ConditionRegistry conditionRegistry;

    public AlertRuleEngine(ConditionRegistry conditionRegistry) {
        this.conditionRegistry = conditionRegistry;
    }

    public List<AlertRecord> evaluateAll(DeviceData data) {
        String sensorType = data.getType();
        //System.out.println("All conditions: " + conditionRegistry.getAllConditions().stream().filter(condition -> sensorType.equals(condition.getDeviceType())).count());

        return conditionRegistry.getAllConditions().stream()
                .filter(condition -> sensorType.equals(condition.getDeviceType()))
                .filter(condition -> evaluateSingleCondition(condition, data))
                .map(condition -> new AlertRecord(
                        data.getDeviceId(),
                        condition.getSeverity(),
                        System.currentTimeMillis(),
                        condition.getDescription()
                ))
                .toList();
    }

    private boolean evaluateSingleCondition(AlertCondition condition, DeviceData data) {
        Map<String, Object> payload = data.getData();
        if (!payload.containsKey(condition.getParameter())) {
            return false;
        }

        Object actualValue = payload.get(condition.getParameter());
        System.out.println("Payload compare " + condition.getValue() + " - " + condition.getOperator() + " - " + actualValue + " - " + payload);
        return compareValues(actualValue, condition.getValue(), condition.getOperator());
    }

    private boolean compareValues(Object actual, String expectedValue, ComparisonOperator op) {
        if (actual instanceof Number) {
            double actualDouble = ((Number) actual).doubleValue();
            double expectedDouble;
            try {
                expectedDouble = Double.parseDouble(expectedValue);
            } catch (NumberFormatException e) {
                return false;
            }

            return switch (op) {
                case GREATER_THAN -> actualDouble > expectedDouble;
                case LESS_THAN -> actualDouble < expectedDouble;
                case EQUALS -> actualDouble == expectedDouble;
                case GREATER_OR_EQUAL -> actualDouble >= expectedDouble;
                case LESS_OR_EQUAL -> actualDouble <= expectedDouble;
                case NOT_EQUALS -> actualDouble != expectedDouble;
            };

        } else if (actual instanceof Boolean) {
            boolean actualBool = (Boolean) actual;
            boolean expectedBool = Boolean.parseBoolean(expectedValue);
            return switch (op) {
                case EQUALS -> actualBool == expectedBool;
                case NOT_EQUALS -> actualBool != expectedBool;
                default -> false;
            };

        } else if (actual instanceof String) {
            return switch (op) {
                case EQUALS -> actual.equals(expectedValue);
                case NOT_EQUALS -> !actual.equals(expectedValue);
                default -> false;
            };

        } else {
            return false;
        }
    }
}

