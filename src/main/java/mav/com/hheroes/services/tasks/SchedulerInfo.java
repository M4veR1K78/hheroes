package mav.com.hheroes.services.tasks;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import mav.com.hheroes.domain.Fille;

public class SchedulerInfo {
	private ScheduledExecutorService scheduler;

	private Fille fille;

	private ScheduledFuture<?> response;

	public SchedulerInfo(ScheduledExecutorService scheduler, Fille fille, ScheduledFuture<?> response) {
		this.scheduler = scheduler;
		this.fille = fille;
		this.response = response;
	}

	public ScheduledExecutorService getScheduler() {
		return scheduler;
	}

	public void setScheduler(ScheduledExecutorService scheduler) {
		this.scheduler = scheduler;
	}

	public Fille getFille() {
		return fille;
	}

	public void setFille(Fille fille) {
		this.fille = fille;
	}

	public ScheduledFuture<?> getResponse() {
		return response;
	}

	public void setResponse(ScheduledFuture<?> response) {
		this.response = response;
	}
}
