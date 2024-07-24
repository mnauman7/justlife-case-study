package org.nauman.app.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class UserAppointmentDTO {
	
	private Integer appointmentId;
	
    private Integer userId;
    
    private Integer startTimeSlotId;
    
    private Integer duration;
    
    private LocalDate appointmentDate;
    
    private Integer serviceType;
    
    private Integer vehicleId;

    private String address;
    
    private String city;

    private LocalDateTime createdDate;
    
    private LocalDateTime updatedDate;

    
	public Integer getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getStartTimeSlotId() {
		return startTimeSlotId;
	}

	public void setStartTimeSlotId(Integer startTimeSlotId) {
		this.startTimeSlotId = startTimeSlotId;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public LocalDate getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(LocalDate appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public Integer getServiceType() {
		return serviceType;
	}

	public void setServiceType(Integer serviceType) {
		this.serviceType = serviceType;
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

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
	}
    
}
