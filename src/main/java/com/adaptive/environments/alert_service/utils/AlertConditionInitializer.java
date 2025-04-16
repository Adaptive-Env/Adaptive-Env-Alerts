package com.adaptive.environments.alert_service.utils;

import com.adaptive.environments.alert_service.model.alert.AlertCondition;
import com.adaptive.environments.alert_service.model.alert.AlertSeverity;
import com.adaptive.environments.alert_service.model.alert.ComparisonOperator;
import com.adaptive.environments.alert_service.repository.AlertConditionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlertConditionInitializer implements CommandLineRunner {

    private final AlertConditionRepository repository;

    public AlertConditionInitializer(AlertConditionRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() == 0) {
            List<AlertCondition> initialConditions = List.of(
                    new AlertCondition("1", "rpm", "value", AlertSeverity.CRITICAL, ComparisonOperator.GREATER_THAN, "1000", "RPM too high"),
                    new AlertCondition("2", "fluid_temp_out", "value", AlertSeverity.WARNING, ComparisonOperator.GREATER_THAN, "20", "Fluid output too hot"),
                    new AlertCondition("3", "pressure_out", "value", AlertSeverity.WARNING, ComparisonOperator.LESS_THAN, "1.5", "Output pressure too low")
            );
            repository.saveAll(initialConditions);
            System.out.println("✅ Initial alert conditions added to MongoDB");
        }
    }
}

