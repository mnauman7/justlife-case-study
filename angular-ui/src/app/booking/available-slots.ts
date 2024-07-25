import { Staff } from "./staff";

export interface AvailableSlots {
  date: string;
  startingTime: string;
  startingTimeId: number;
  duration: number;
  availableStaff: Staff[];

  staff1: Staff;
  staff2: Staff;
  staff3: Staff;
}
