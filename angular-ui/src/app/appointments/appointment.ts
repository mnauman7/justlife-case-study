export interface Appointment {
  appointmentId: number;
  userId: number;
  startTimeSlotId: number;
  startTime: string;
  duration: number;
  appointmentDate: string;
  serviceType: number;
  vehicleId: number;
}
