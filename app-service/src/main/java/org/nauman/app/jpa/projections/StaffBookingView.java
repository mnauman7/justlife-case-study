package org.nauman.app.jpa.projections;

/**
 * To get specific columns from StaffEntity 
 */
public interface StaffBookingView {
	
	public Integer getStaffId();
	
	public String getFirstName();
	
	public String getLastName();
	
	public Integer getVehicleId();
}
