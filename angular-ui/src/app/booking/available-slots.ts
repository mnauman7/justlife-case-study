import { Staff } from "./staff";

export interface AvailableSlots {
  date: string;
  startingTime: string;
  availableStaff: Staff[];
}
