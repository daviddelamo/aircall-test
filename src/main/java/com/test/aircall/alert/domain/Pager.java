package com.test.aircall.alert.domain;

import com.test.aircall.alert.adapter.out.persistence.PagerRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Pager {

    private final Notifier notifier;
    private final PagerRepository pagerRepository;

    public void receiveAlert(String serviceId){
        MonitoredService monitoredServiceStatus = pagerRepository.getServiceStatus(serviceId);
        if (monitoredServiceStatus.isHealthy()){
            monitoredServiceStatus.setHealthy(false);
            monitoredServiceStatus.setAlertAcknowledged(false);
            monitoredServiceStatus.setEscalationLevel(1);
            pagerRepository.updateService(monitoredServiceStatus);
            notifier.notifyTargets(monitoredServiceStatus);
        }
    }

    public void receiveHealthyEvent(String serviceId){
        MonitoredService monitoredServiceStatus = pagerRepository.getServiceStatus(serviceId);
        if (!monitoredServiceStatus.isHealthy()){
            monitoredServiceStatus.setHealthy(true);
            monitoredServiceStatus.setAlertAcknowledged(true);
            monitoredServiceStatus.setEscalationLevel(1);
            pagerRepository.updateService(monitoredServiceStatus);
        }
    }

    public void receiveAcknowledgement(String serviceId){
        MonitoredService monitoredServiceStatus = pagerRepository.getServiceStatus(serviceId);
        if (!monitoredServiceStatus.isHealthy()){
            monitoredServiceStatus.setAlertAcknowledged(true);
            pagerRepository.updateService(monitoredServiceStatus);
        }
    }

    public void receiveAlertFromTimeout(String serviceId){
        MonitoredService monitoredServiceStatus = pagerRepository.getServiceStatus(serviceId);
        if (!monitoredServiceStatus.isHealthy() && !monitoredServiceStatus.isAlertAcknowledged()){
            int currentLevel = monitoredServiceStatus.getEscalationLevel();
            currentLevel++;
            monitoredServiceStatus.setEscalationLevel(currentLevel);
            pagerRepository.updateService(monitoredServiceStatus);
            notifier.notifyTargets(monitoredServiceStatus);

        }
    }
}
