package com.adaptive.environments.alert_service.service;

import com.adaptive.environments.alert_service.kafka.KafkaAlertProducer;
import com.adaptive.environments.alert_service.model.alert.AlertRecord;
import com.adaptive.environments.alert_service.model.data.DeviceData;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertReactionService {

    private final AlertRuleEngine ruleEngine;
    private final MeterRegistry meterRegistry;
    private final KafkaAlertProducer kafkaAlertProducer;

    public AlertReactionService(AlertRuleEngine ruleEngine,
                                MeterRegistry meterRegistry,
                                KafkaAlertProducer kafkaAlertProducer) {
        this.ruleEngine = ruleEngine;
        this.meterRegistry = meterRegistry;
        this.kafkaAlertProducer = kafkaAlertProducer;
    }


    public List<AlertRecord> evaluate(DeviceData data) {
        List<AlertRecord> alertOpt = ruleEngine.evaluateAll(data);

        alertOpt.forEach(alert -> {
            meterRegistry.counter("iot.alerts.triggered",
                    "type", alert.getType().name(),
                    "severity", alert.getSeverity().name()
            ).increment();

            kafkaAlertProducer.sendAlert("iot.alerts", alert);
        });


        return alertOpt;
    }
}
