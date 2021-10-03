package com.test.aircall.alert.adapter.out.rest;

import com.test.aircall.alert.adapter.out.rest.dto.NotificationTarget;
import com.test.aircall.alert.domain.MonitoredService;
import lombok.NonNull;

import java.util.List;

public interface EscalationPolicy {

    /**
     * Returs a list of notification target for the identified service and the corresponding escalation level
     * @param monitoredService The service to be queried
     * @return A list of notification targets for this service and escalation level.
     * The list will be empty if there is no targets to notify for the specified level
     */
    @NonNull List<NotificationTarget> getNotificationTargetByEscalationLevel(MonitoredService monitoredService);

}
