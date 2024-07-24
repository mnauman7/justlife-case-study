package org.nauman.app.enums;

/**
 * Enum to store the type of situations which staff can be occupied with
 * Same options are also stored in database table StaffOccupancyTypeMaster
 */
public enum StaffOccupancyType {
	  WORK(1), TRAVELLING(2), VEHICLE_BUSY(3), BREAK(4), LEAVE(5);

	  private int occupancyType;

	  StaffOccupancyType(int occupancyType) {
	    this.occupancyType = occupancyType;
	  }

	  public int getValue() {
	    return occupancyType;
	  }
}
