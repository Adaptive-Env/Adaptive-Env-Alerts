package com.adaptive.environments.alert_service.registry;

import com.adaptive.environments.alert_service.model.alert.AlertCondition;
import com.adaptive.environments.alert_service.model.alert.AlertSeverity;
import com.adaptive.environments.alert_service.model.alert.ComparisonOperator;
import com.adaptive.environments.alert_service.repository.AlertConditionRepository;
import com.adaptive.environments.alert_service.service.AlertConditionValidator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ConditionRegistry {

    private final AlertConditionValidator validator;
    private final AlertConditionRepository repository;

    public ConditionRegistry(AlertConditionValidator validator, AlertConditionRepository repository) {
        this.validator = validator;
        this.repository = repository;
    }

    public List<AlertCondition> getAllConditions() {
        return repository.findAll();
    }

    public AlertCondition getCondition(String id) {
        return repository.findById(id).orElse(null);
    }

    public String addCondition(AlertCondition condition) {
        if (!validator.validate(condition)) {
            throw new IllegalArgumentException("Invalid alert condition.");
        }
        AlertCondition saved = repository.save(condition);
        return saved.getId();
    }

    public void updateCondition(String id, AlertCondition updated) {
        if (!validator.validate(updated)) {
            throw new IllegalArgumentException("Invalid alert condition.");
        }
        updated.setId(id);
        repository.save(updated);
    }

    public void removeCondition(String id) {
        repository.deleteById(id);
    }

    public boolean contains(String id) {
        return repository.existsById(id);
    }
}

