package com.test.aircall.alert.domain;

import com.test.aircall.alert.adapter.out.rest.EscalationPolicy;
import com.test.aircall.alert.adapter.out.rest.Mail;
import com.test.aircall.alert.adapter.out.rest.SMS;
import com.test.aircall.alert.adapter.out.rest.Timer;
import com.test.aircall.alert.adapter.out.rest.dto.EmailNotificationTarget;
import com.test.aircall.alert.adapter.out.rest.dto.NotificationTarget;
import com.test.aircall.alert.adapter.out.rest.dto.SMSNotificationTarget;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.List;

@RequiredArgsConstructor
public class Notifier {

    private final Timer timerAdapter;
    private final EscalationPolicy escalationPolicy;
    private final SMS SMSAdapter;
    private final Mail mailAdapter;

    private final static String DEFAULT_MESSAGE = "The service %s is down.";
    private final static Duration DEFAULT_TIMEOUT_DURATION = Duration.ofMinutes(15);

    /**
     * Notify the alert to the escalation targets and set the timeout for the acknowledgment,
     * in case there is no more targets for escalation do nothing.
     * @param monitoredServiceStatus The service that had received the alert.
     */
    public void notifyTargets(MonitoredService monitoredServiceStatus){
        List<NotificationTarget> notificationTargetList =  escalationPolicy.getNotificationTargetByEscalationLevel(monitoredServiceStatus);
        if (notificationTargetList.size() == 0){
            return;
        }
        String msg = String.format(DEFAULT_MESSAGE, monitoredServiceStatus.getName());
        notificationTargetList.forEach(notificationTarget -> {
            if (notificationTarget instanceof SMSNotificationTarget) {
                SMSAdapter.notifyIncident(((SMSNotificationTarget) notificationTarget).getMobileNumber(), msg);
            } else {
                mailAdapter.notifyIncident(((EmailNotificationTarget) notificationTarget).getEmail(), msg);
            }
        });
        timerAdapter.setTimeout(monitoredServiceStatus.getId(), DEFAULT_TIMEOUT_DURATION);
    }
}
