package com.adaptive.environments.alert_service.service;

import com.adaptive.environments.alert_service.model.alert.AlertCondition;
import com.adaptive.environments.alert_service.model.alert.AlertDTO;
import com.adaptive.environments.alert_service.model.alert.AlertType;
import com.adaptive.environments.alert_service.model.alert.ComparisonOperator;
import com.adaptive.environments.alert_service.model.data.DeviceData;
import com.adaptive.environments.alert_service.registry.ConditionRegistry;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Component
public class AlertRuleEngine {

    private final ConditionRegistry conditionRegistry;

    public AlertRuleEngine(ConditionRegistry conditionRegistry) {
        this.conditionRegistry = conditionRegistry;
    }


    public List<AlertDTO> evaluateAll(DeviceData data) {
        String deviceType = data.getType();

        return conditionRegistry.getAllConditions().stream()
                .filter(condition -> deviceType.equals(condition.getDeviceType()))
                .filter(condition -> evaluateSingleCondition(condition, data))
                .map(condition -> new AlertDTO(
                        data.getDeviceId(),
                        AlertType.valueOf(condition.getParameter().toUpperCase()),
                        condition.getSeverity(),
                        System.currentTimeMillis(),
                        condition.getDescription()
                ))
                .toList();
    }


    private boolean evaluateSingleCondition(AlertCondition condition, DeviceData data) {
        try {
            Field field = data.getClass().getDeclaredField(condition.getParameter());
            field.setAccessible(true);
            Object actualValue = field.get(data);

            return compareValues(actualValue, condition.getValue(), condition.getOperator());

        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("Alert evaluation failed: " + e.getMessage());
            return false;
        }
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
