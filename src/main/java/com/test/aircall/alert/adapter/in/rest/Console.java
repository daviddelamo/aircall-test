package com.test.aircall.alert.adapter.in.rest;

import com.test.aircall.alert.domain.MonitoredService;

public interface Console {
    void acknowledgeAlert(MonitoredService monitoredService);

}
