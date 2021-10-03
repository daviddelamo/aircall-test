package com.test.aircall.alert.domain;

import lombok.Data;

@Data
public class MonitoredService {
    private String id;
    private String name;
    private boolean healthy;
    private int escalationLevel;
    private boolean alertAcknowledged;
}
