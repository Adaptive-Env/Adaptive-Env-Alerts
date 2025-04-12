package com.adaptive.environments.alert_service.service;

import com.adaptive.environments.alert_service.kafka.KafkaAlertProducer;
import com.adaptive.environments.alert_service.model.alert.AlertDTO;
import com.adaptive.environments.alert_service.model.data.DeviceData;
import com.adaptive.environments.alert_service.notifier.AlertNotifier;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertReactionService {

    private final AlertRuleEngine ruleEngine;
    private final MeterRegistry meterRegistry;
    private final List<AlertNotifier> notifiers;
    private final KafkaAlertProducer kafkaAlertProducer;

    public AlertReactionService(AlertRuleEngine ruleEngine,
                                MeterRegistry meterRegistry,
                                List<AlertNotifier> notifiers,
                                KafkaAlertProducer kafkaAlertProducer) {
        this.ruleEngine = ruleEngine;
        this.meterRegistry = meterRegistry;
        this.notifiers = notifiers;
        this.kafkaAlertProducer = kafkaAlertProducer;
    }


    public List<AlertDTO> evaluate(DeviceData data) {
        List<AlertDTO> alertOpt = ruleEngine.evaluateAll(data);

        alertOpt.forEach(alert -> {
            meterRegistry.counter("iot.alerts.triggered",
                    "type", alert.getType().name(),
                    "severity", alert.getSeverity().name()
            ).increment();

            kafkaAlertProducer.sendAlert("iot.alerts", alert);

            notifiers.forEach(notifier -> notifier.notify(alert));
        });


        return alertOpt;
    }
}
