package com.adaptive.environments.alert_service.service;

import com.adaptive.environments.alert_service.model.alert.AlertCondition;
import com.adaptive.environments.alert_service.model.alert.ComparisonOperator;
import com.adaptive.environments.alert_service.model.data.DeviceData;
import com.adaptive.environments.alert_service.registry.SensorTypeRegistry;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class AlertConditionValidator {

    private final SensorTypeRegistry registry;

    public AlertConditionValidator(SensorTypeRegistry registry) {
        this.registry = registry;
    }

    public boolean validate(AlertCondition condition) {
        return validateOperator(condition)
                && validateFieldAndType(condition.getDeviceType(), condition.getParameter(), condition.getValue(), condition.getOperator());
    }

    private boolean validateOperator(AlertCondition condition) {
        try {
            if (requiresNumericValue(condition.getOperator())) {
                Double.parseDouble(condition.getValue());
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean validateFieldAndType(String sensorType, String field, String value, ComparisonOperator op) {
        var schema = registry.getSchemaFor(sensorType);
        if (schema == null || !schema.containsKey(field)) {
            return false;
        }

        Class<?> expectedType = schema.get(field);

        try {
            if (requiresNumericValue(op)) {
                return Number.class.isAssignableFrom(expectedType)
                        && !Double.isNaN(Double.parseDouble(value));
            }

            if (isBooleanOperator(op)) {
                return expectedType == Boolean.class || expectedType == boolean.class;
            }

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    private boolean requiresNumericValue(ComparisonOperator op) {
        return switch (op) {
            case GREATER_THAN, GREATER_OR_EQUAL, LESS_THAN, LESS_OR_EQUAL -> true;
            default -> false;
        };
    }

    private boolean isBooleanOperator(ComparisonOperator op) {
        return op == ComparisonOperator.EQUALS || op == ComparisonOperator.NOT_EQUALS;
    }
}
