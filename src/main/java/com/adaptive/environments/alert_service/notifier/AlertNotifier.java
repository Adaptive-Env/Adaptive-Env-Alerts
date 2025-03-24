package com.adaptive.environments.alert_service.notifier;

import com.adaptive.environments.alert_service.model.alert.AlertDTO;

public interface AlertNotifier {
    public void notify(AlertDTO message);
}

