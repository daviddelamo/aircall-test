package com.test.aircall;

import com.test.aircall.alert.adapter.out.persistence.PagerRepository;
import com.test.aircall.alert.adapter.out.rest.EscalationPolicy;
import com.test.aircall.alert.adapter.out.rest.Mail;
import com.test.aircall.alert.adapter.out.rest.SMS;
import com.test.aircall.alert.adapter.out.rest.Timer;
import com.test.aircall.alert.adapter.out.rest.dto.EmailNotificationTarget;
import com.test.aircall.alert.adapter.out.rest.dto.NotificationTarget;
import com.test.aircall.alert.adapter.out.rest.dto.SMSNotificationTarget;
import com.test.aircall.alert.domain.Notifier;
import com.test.aircall.alert.domain.Pager;
import com.test.aircall.alert.domain.MonitoredService;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


class AlertNotificationSystemApplicationTests {

	@Test
	void useCase1(){
		String serviceId = "id1";
		MonitoredService monitoredService = new MonitoredService();
		monitoredService.setHealthy(true);
		monitoredService.setId(serviceId);
		monitoredService.setName("Service1");

		NotificationTarget mailTarget = (EmailNotificationTarget) () -> "testAirCall@aircall.com";

		NotificationTarget smsNotificationTarget = (SMSNotificationTarget) () -> "+0015551119999";

		List<NotificationTarget> notificationTargetList = Arrays.asList(mailTarget, smsNotificationTarget);

		PagerRepository pagerRepository = mock(PagerRepository.class);
		Timer timerAdapter = mock(Timer.class);
		EscalationPolicy escalationPolicyAdapter = mock(EscalationPolicy.class);
		SMS smsService = mock(SMS.class);
		Mail mailService = mock(Mail.class);
		when(pagerRepository.getServiceStatus(serviceId)).thenReturn(monitoredService);
		when(escalationPolicyAdapter.getNotificationTargetByEscalationLevel(monitoredService)).thenReturn(notificationTargetList);

		Notifier notifier = new Notifier(timerAdapter, escalationPolicyAdapter,smsService, mailService);
		Pager pagerService = new Pager(notifier, pagerRepository);
		pagerService.receiveAlert(serviceId);

		verify(pagerRepository).getServiceStatus(serviceId);
		verify(escalationPolicyAdapter).getNotificationTargetByEscalationLevel(monitoredService);
		verify(timerAdapter).setTimeout(serviceId, Duration.ofMinutes(15));
		verify(smsService, times(1)).notifyIncident(any(), any());
		verify(mailService, times(1)).notifyIncident(any(), any());
	}

	@Test
	void useCase2(){
		String serviceId = "id1";
		MonitoredService monitoredService = new MonitoredService();
		monitoredService.setHealthy(false);
		monitoredService.setId(serviceId);
		monitoredService.setName("Service1");
		monitoredService.setEscalationLevel(1);

		NotificationTarget mailTarget = (EmailNotificationTarget) () -> "testAirCall@aircall.com";

		NotificationTarget smsNotificationTarget = (SMSNotificationTarget) () -> "+0015551119999";

		List<NotificationTarget> notificationTargetList = Arrays.asList(mailTarget, smsNotificationTarget);

		PagerRepository pagerRepository = mock(PagerRepository.class);
		Timer timerAdapter = mock(Timer.class);
		EscalationPolicy escalationPolicyAdapter = mock(EscalationPolicy.class);
		SMS smsService = mock(SMS.class);
		Mail mailService = mock(Mail.class);
		when(pagerRepository.getServiceStatus(serviceId)).thenReturn(monitoredService);
		when(escalationPolicyAdapter.getNotificationTargetByEscalationLevel(monitoredService)).thenReturn(notificationTargetList);

		Notifier notifier = new Notifier(timerAdapter, escalationPolicyAdapter,smsService, mailService);
		Pager pagerService = new Pager(notifier, pagerRepository);
		pagerService.receiveAlertFromTimeout(serviceId);

		assertEquals(2, monitoredService.getEscalationLevel());

		verify(pagerRepository).getServiceStatus(serviceId);
		verify(escalationPolicyAdapter).getNotificationTargetByEscalationLevel(monitoredService);
		verify(timerAdapter).setTimeout(serviceId, Duration.ofMinutes(15));
		verify(smsService, times(1)).notifyIncident(any(), any());
		verify(mailService, times(1)).notifyIncident(any(), any());
	}

	@Test
	void useCase3(){
		String serviceId = "id1";
		MonitoredService monitoredService = new MonitoredService();
		monitoredService.setHealthy(false);
		monitoredService.setId(serviceId);
		monitoredService.setName("Service1");
		monitoredService.setEscalationLevel(1);

		PagerRepository pagerRepository = mock(PagerRepository.class);
		Timer timerAdapter = mock(Timer.class);
		EscalationPolicy escalationPolicyAdapter = mock(EscalationPolicy.class);
		SMS smsService = mock(SMS.class);
		Mail mailService = mock(Mail.class);
		when(pagerRepository.getServiceStatus(serviceId)).thenReturn(monitoredService);

		Notifier notifier = new Notifier(timerAdapter, escalationPolicyAdapter,smsService, mailService);
		Pager pagerService = new Pager(notifier, pagerRepository);
		pagerService.receiveAcknowledgement(serviceId);

		when(pagerRepository.getServiceStatus(serviceId)).thenReturn(monitoredService);

		pagerService.receiveAlertFromTimeout(serviceId);

		verify(pagerRepository, times(2)).getServiceStatus(serviceId);
		verify(escalationPolicyAdapter, times(0)).getNotificationTargetByEscalationLevel(monitoredService);
		verify(timerAdapter, times(0)).setTimeout(serviceId, Duration.ofMinutes(15));
		verify(smsService, times(0)).notifyIncident(any(), any());
		verify(mailService, times(0)).notifyIncident(any(), any());
	}


	@Test
	void useCase4(){
		String serviceId = "id1";
		MonitoredService monitoredService = new MonitoredService();
		monitoredService.setHealthy(false);
		monitoredService.setId(serviceId);
		monitoredService.setName("Service1");
		monitoredService.setEscalationLevel(1);


		PagerRepository pagerRepository = mock(PagerRepository.class);
		Timer timerAdapter = mock(Timer.class);
		EscalationPolicy escalationPolicyAdapter = mock(EscalationPolicy.class);
		SMS smsService = mock(SMS.class);
		Mail mailService = mock(Mail.class);
		when(pagerRepository.getServiceStatus(serviceId)).thenReturn(monitoredService);

		Notifier notifier = new Notifier(timerAdapter, escalationPolicyAdapter,smsService, mailService);
		Pager pagerService = new Pager(notifier, pagerRepository);
		pagerService.receiveAlert(serviceId);

		verify(pagerRepository).getServiceStatus(serviceId);
		verify(escalationPolicyAdapter, times(0)).getNotificationTargetByEscalationLevel(monitoredService);
		verify(timerAdapter, times(0)).setTimeout(serviceId, Duration.ofMinutes(15));
		verify(smsService, times(0)).notifyIncident(any(), any());
		verify(mailService, times(0)).notifyIncident(any(), any());
	}

	@Test
	void useCase5(){
		String serviceId = "id1";
		MonitoredService monitoredService = new MonitoredService();
		monitoredService.setHealthy(false);
		monitoredService.setId(serviceId);
		monitoredService.setName("Service1");
		monitoredService.setEscalationLevel(1);

		PagerRepository pagerRepository = mock(PagerRepository.class);
		Timer timerAdapter = mock(Timer.class);
		EscalationPolicy escalationPolicyAdapter = mock(EscalationPolicy.class);
		SMS smsService = mock(SMS.class);
		Mail mailService = mock(Mail.class);
		when(pagerRepository.getServiceStatus(serviceId)).thenReturn(monitoredService);

		Notifier notifier = new Notifier(timerAdapter, escalationPolicyAdapter,smsService, mailService);
		Pager pagerService = new Pager(notifier, pagerRepository);
		pagerService.receiveHealthyEvent(serviceId);

		assertTrue(monitoredService.isHealthy());
		verify(pagerRepository).getServiceStatus(serviceId);
		verify(escalationPolicyAdapter, times(0)).getNotificationTargetByEscalationLevel(monitoredService);
		verify(timerAdapter, times(0)).setTimeout(serviceId, Duration.ofMinutes(15));
		verify(smsService, times(0)).notifyIncident(any(), any());
		verify(mailService, times(0)).notifyIncident(any(), any());
	}

}
