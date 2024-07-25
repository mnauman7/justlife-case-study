package org.nauman.app.jpa.entity;

import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "TimeSlots")
public class TimeSlotEntity {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slot_id", unique = true, nullable = false)
	private Integer slotId;
	
    @Column(name = "start_time", nullable = false)
	private LocalTime startTime;
	
    @Column(name = "end_time", nullable = false)
	private LocalTime endTime;
	
    @Column(name = "slot_name", nullable = false)
	private String slotName;
    
	@OneToMany(mappedBy = "timeSlot", fetch = FetchType.LAZY)
    private List<StaffOccupancyEntity> staffOccupancy;

    
	public Integer getSlotId() {
		return slotId;
	}

	public void setSlotId(Integer slotId) {
		this.slotId = slotId;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public String getSlotName() {
		return slotName;
	}

	public void setSlotName(String slotName) {
		this.slotName = slotName;
	}

	public List<StaffOccupancyEntity> getStaffOccupancy() {
		return staffOccupancy;
	}

	public void setStaffOccupancy(List<StaffOccupancyEntity> staffOccupancy) {
		this.staffOccupancy = staffOccupancy;
	}
	
}
