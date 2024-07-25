package org.nauman.app.model;

import java.time.LocalDate;

public class UpdateAppointmentRequestDTO {
		
	private Integer startingTimeId;
	
	private LocalDate appointmentDate;


	public Integer getStartingTimeId() {
		return startingTimeId;
	}

	public void setStartingTimeId(Integer startingTimeId) {
		this.startingTimeId = startingTimeId;
	}

	public LocalDate getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(LocalDate appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

}
