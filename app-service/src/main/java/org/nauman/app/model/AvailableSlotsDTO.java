package org.nauman.app.model;

import java.util.List;

public class AvailableSlotsDTO {
	
	private String startingTime;
	
	private Integer startingTimeId;
	
	private Integer duration;
	
	private String date;
	
	private List<StaffBookingDTO> availableStaff;
	

	public String getStartingTime() {
		return startingTime;
	}

	public void setStartingTime(String startingTime) {
		this.startingTime = startingTime;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List<StaffBookingDTO> getAvailableStaff() {
		return availableStaff;
	}

	public void setAvailableStaff(List<StaffBookingDTO> availableStaff) {
		this.availableStaff = availableStaff;
	}

	public Integer getStartingTimeId() {
		return startingTimeId;
	}

	public void setStartingTimeId(Integer startingTimeId) {
		this.startingTimeId = startingTimeId;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	
	
	
}
