package com.adaptive.environments.alert_service.kafka;


import com.adaptive.environments.alert_service.model.data.DeviceData;
import com.adaptive.environments.alert_service.model.data.ValidatedData;
import com.adaptive.environments.alert_service.service.AlertReactionService;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;

@Service
public class KafkaValidatedDataConsumer {

    private static final Logger log = LoggerFactory.getLogger(KafkaValidatedDataConsumer.class);

    private final KafkaReceiver<String, ValidatedData> kafkaReceiver;
    private final AlertReactionService alertReactionService;
    private final MeterRegistry meterRegistry;

    public KafkaValidatedDataConsumer(KafkaReceiver<String, ValidatedData> kafkaReceiver,
                                      AlertReactionService alertReactionService,
                                      MeterRegistry meterRegistry) {
        this.kafkaReceiver = kafkaReceiver;
        this.alertReactionService = alertReactionService;
        this.meterRegistry = meterRegistry;

        startReceiving();
    }

    private void startReceiving() {
        kafkaReceiver.receive()
                .doOnNext(this::processRecord)
                .subscribe();
    }

    private void processRecord(ReceiverRecord<String, ValidatedData> record) {
        try {
            ValidatedData validatedData = record.value();
            DeviceData deviceData = validatedData.getDeviceData();

            alertReactionService.evaluate(deviceData);
            meterRegistry.counter("iot.alerts.checked", "deviceType", deviceData.getType()).increment();

        } catch (Exception e) {
            log.error("[Kafka] Error during alert evaluation", e);
            meterRegistry.counter("iot.alerts.error", "reason", "exception").increment();
        } finally {
            record.receiverOffset().acknowledge();
        }
    }
}
