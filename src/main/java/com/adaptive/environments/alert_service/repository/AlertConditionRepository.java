package com.adaptive.environments.alert_service.repository;

import com.adaptive.environments.alert_service.model.alert.AlertCondition;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertConditionRepository extends MongoRepository<AlertCondition, String> {
    List<AlertCondition> findByDeviceType(String deviceType);
}