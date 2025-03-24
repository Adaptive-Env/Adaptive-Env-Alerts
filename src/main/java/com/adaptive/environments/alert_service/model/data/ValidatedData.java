package com.adaptive.environments.alert_service.model.data;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidatedData {
    private DeviceData deviceData;
    private String hash;


}
