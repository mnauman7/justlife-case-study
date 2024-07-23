package org.nauman.app.model;

import java.time.LocalDate;
import java.util.List;

public class CreateAppointmentRequestDTO {
	
	private Integer userId;
	
	private Integer startingTimeId;
	
	private Integer duration;
	
	private LocalDate appointmentDate;
	
	private Integer serviceTypeId;
	
	private Integer vehicleId;
	
	private String address;
	
	private String city;
	
	private List<StaffDTO> requiredStaff;

	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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

	public Integer getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(Integer serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	public Integer getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(Integer vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public List<StaffDTO> getRequiredStaff() {
		return requiredStaff;
	}

	public void setRequiredStaff(List<StaffDTO> requiredStaff) {
		this.requiredStaff = requiredStaff;
	}

	public LocalDate getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(LocalDate appointmentDate) {
		this.appointmentDate = appointmentDate;
	}
	
}
