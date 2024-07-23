package org.nauman.app.enums;

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
