package org.nauman.app.jpa.entity;

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
@Table(name = "Vehicles")
public class VehicleEntity {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id", unique = true, nullable = false)
	private Integer vehicleId;
    
	@Column(name = "vehicle_name", nullable = false)
    private String vehicleName;
    
    @Column(name = "capacity", nullable = false)
    private Integer capacity;
    
    @Column(name = "phone")
    private String phone;
    
    @Column(name = "is_active", columnDefinition = "TINYINT(1)")
    private Boolean isActive;
    
	@OneToMany(mappedBy = "vehicle", fetch = FetchType.LAZY)
    private List<StaffEntity> staff;


	public Integer getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(Integer vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getVehicleName() {
		return vehicleName;
	}

	public void setVehicleName(String vehicleName) {
		this.vehicleName = vehicleName;
	}

	public Integer getCapacity() {
		return capacity;
	}

	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
	
	public List<StaffEntity> getStaff() {
		return staff;
	}

	public void setStaff(List<StaffEntity> staff) {
		this.staff = staff;
	}
    
}
