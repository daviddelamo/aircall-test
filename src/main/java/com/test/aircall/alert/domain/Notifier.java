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

    public void notifyTargets(MonitoredService monitoredServiceStatus){
        List<NotificationTarget> notificationTargetList =  escalationPolicy.getNotificationTargetByEscalationLevel(monitoredServiceStatus);
        if (notificationTargetList.size() == 0){
            return;
        }
        String msg = String.format("The service %s is down.", monitoredServiceStatus.getName());
        notificationTargetList.forEach(notificationTarget -> {
            if (notificationTarget instanceof SMSNotificationTarget) {
                SMSAdapter.notifyIncident(((SMSNotificationTarget) notificationTarget).getMobileNumber(), msg);
            } else {
                mailAdapter.notifyIncident(((EmailNotificationTarget) notificationTarget).getEmail(), msg);
            }
        });
        timerAdapter.setTimeout(monitoredServiceStatus.getId(), Duration.ofMinutes(15));
    }
}
