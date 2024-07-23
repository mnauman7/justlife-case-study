import { Staff } from "./staff";

export interface AppointmentRequest {
  userId: number;
  startingTimeId: number;
  duration: number;
  appointmentDate: string;
  serviceTypeId: number;
  vehicleId: number;
  address: string;
  city: string;
  requiredStaff: Staff[];
}
