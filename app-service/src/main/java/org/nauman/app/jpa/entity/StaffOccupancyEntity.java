package org.nauman.app.jpa.entity;

import java.time.LocalDate;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "StaffOccupancy")
public class StaffOccupancyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "occupancy_id", unique = true, nullable = false)
	private Integer occupancyId;
    
    @Column(name = "staff_id", nullable = false)
	private Integer staffId;
    
    @Column(name = "time_slot_id", nullable = false)
    private Integer timeSlotId;
    
    @Column(name = "occupancy_date", nullable = false)
    private LocalDate occupancyDate;
    
    @Column(name = "occupancy_type_id", nullable = false)
    private Integer occupancyTypeId;
    
    @Column(name = "appointment_id")
    private Integer appointmentId;
    

	public Integer getOccupancyId() {
		return occupancyId;
	}

	public void setOccupancyId(Integer occupancyId) {
		this.occupancyId = occupancyId;
	}

	public Integer getStaffId() {
		return staffId;
	}

	public void setStaffId(Integer staffId) {
		this.staffId = staffId;
	}

	public Integer getTimeSlotId() {
		return timeSlotId;
	}

	public void setTimeSlotId(Integer timeSlotId) {
		this.timeSlotId = timeSlotId;
	}

	public LocalDate getOccupancyDate() {
		return occupancyDate;
	}

	public void setOccupancyDate(LocalDate occupancyDate) {
		this.occupancyDate = occupancyDate;
	}

	public Integer getOccupancyTypeId() {
		return occupancyTypeId;
	}

	public void setOccupancyTypeId(Integer occupancyTypeId) {
		this.occupancyTypeId = occupancyTypeId;
	}

	public Integer getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}
    
	
}
