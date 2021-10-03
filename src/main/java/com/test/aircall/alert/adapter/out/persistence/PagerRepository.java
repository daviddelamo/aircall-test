package com.test.aircall.alert.adapter.out.persistence;

import com.test.aircall.alert.domain.MonitoredService;

public interface PagerRepository {
    void updateService(MonitoredService monitoredService);
    MonitoredService getServiceStatus(String serviceId);
}
