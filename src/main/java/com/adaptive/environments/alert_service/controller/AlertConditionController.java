package com.adaptive.environments.alert_service.controller;

import com.adaptive.environments.alert_service.model.alert.AlertCondition;
import com.adaptive.environments.alert_service.registry.ConditionRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/conditions")
public class AlertConditionController {

    private final ConditionRegistry conditionRegistry;

    public AlertConditionController(ConditionRegistry conditionRegistry) {
        this.conditionRegistry = conditionRegistry;
    }

    @GetMapping
    public Collection<AlertCondition> getAll() {
        return conditionRegistry.getAllConditions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertCondition> get(@PathVariable String id) {
        AlertCondition condition = conditionRegistry.getCondition(id);
        if (condition == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(condition);
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody AlertCondition condition) {
        String id = conditionRegistry.addCondition(condition);
        return ResponseEntity.ok(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody AlertCondition updated) {
        if (conditionRegistry.contains(id)) {
            return ResponseEntity.notFound().build();
        }
        conditionRegistry.updateCondition(id, updated);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (conditionRegistry.contains(id)) {
            return ResponseEntity.notFound().build();
        }
        conditionRegistry.removeCondition(id);
        return ResponseEntity.ok().build();
    }
}