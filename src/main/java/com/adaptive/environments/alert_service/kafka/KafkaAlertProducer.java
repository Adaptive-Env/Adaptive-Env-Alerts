package com.adaptive.environments.alert_service.kafka;

import com.adaptive.environments.alert_service.model.alert.AlertRecord;
import io.micrometer.core.instrument.MeterRegistry;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Component
public class KafkaAlertProducer {

    private static final Logger log = LoggerFactory.getLogger(KafkaAlertProducer.class);

    private final KafkaSender<String, AlertRecord> kafkaSender;
    private final MeterRegistry meterRegistry;

    public KafkaAlertProducer(KafkaSender<String, AlertRecord> kafkaSender,
                              MeterRegistry meterRegistry) {
        this.kafkaSender = kafkaSender;
        this.meterRegistry = meterRegistry;
    }

    public void sendAlert(String topic, AlertRecord alert) {
        ProducerRecord<String, AlertRecord> record = new ProducerRecord<>(topic, alert.getDeviceId(), alert);
        SenderRecord<String, AlertRecord, String> senderRecord = SenderRecord.create(record, null);

        kafkaSender.send(Flux.just(senderRecord))
                .doOnNext(result -> {
                    log.info("[Kafka] Alert sent for device: {}, type: {}, severity: {}",
                            alert.getDeviceId(), alert.getDescription(), alert.getSeverity());
                    meterRegistry.counter("iot.alert.success", "severity", alert.getSeverity().name()).increment();
                })
                .doOnError(e -> {
                    log.error("[Kafka] Failed to send alert: {}", e.getMessage());
                    meterRegistry.counter("iot.alert.failure").increment();
                })
                .subscribe();
    }
}
