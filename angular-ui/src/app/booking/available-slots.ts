import { Staff } from "./staff";

export interface AvailableSlots {
  date: string;
  startingTime: string;
  startingTimeId: number;
  duration: number;
  availableStaff: Staff[];
}
