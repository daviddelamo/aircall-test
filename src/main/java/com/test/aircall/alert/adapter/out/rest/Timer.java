package com.test.aircall.alert.adapter.out.rest;

import java.time.Duration;

public interface Timer {

    /**
     * Make a request to the TimerService in order to set up an reminder that this service has an error and should be acknowledged before the established duration.
     * @param serviceId The identification of the failing service
     * @param duration Time to wait before scaling the alarm level
     */
    void setTimeout(String serviceId, Duration duration);

}
