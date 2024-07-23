package org.nauman.app.model;

import java.util.List;

public class AvailableSlotsDTO {
	
	private String startingTime;
	
	private String date;
	
	private List<StaffDTO> availableStaff;
	

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

	public List<StaffDTO> getAvailableStaff() {
		return availableStaff;
	}

	public void setAvailableStaff(List<StaffDTO> availableStaff) {
		this.availableStaff = availableStaff;
	}
	
}
