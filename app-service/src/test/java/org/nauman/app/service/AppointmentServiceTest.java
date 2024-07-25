package org.nauman.app.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.nauman.app.jpa.entity.TimeSlotEntity;
import org.nauman.app.jpa.repository.AppointmentRepository;
import org.nauman.app.jpa.repository.AppointmentStaffRepository;
import org.nauman.app.jpa.repository.StaffOccupancyRepository;
import org.nauman.app.jpa.repository.StaffRepository;
import org.nauman.app.jpa.repository.TimeSlotsRepository;
import org.nauman.app.model.AvailableSlotsDTO;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class AppointmentServiceTest {
	
	@Mock
	private AppointmentRepository appointmentRepository;
	
	@Mock
	private AppointmentStaffRepository appointmentStaffRepository;
	
	@Mock
	private TimeSlotsRepository timeSlotsRepository;
	
	@Mock
	private StaffRepository staffRepository;
	
	@Mock
	private StaffOccupancyRepository staffOccupancyRepository;
	
	@Mock
	private ModelMapper modelMapper;
	
	@InjectMocks
	private AppointmentService appointmentService;
	
	
	@Test
	public void test_getAllAvailableSlots_withInvalidDates() {
		// passing friday 
		List<AvailableSlotsDTO> availableSlotsOnFriday = appointmentService.getAvailableSlots("2024-07-26",2,2,null);

		assertNotNull(availableSlotsOnFriday);

		// check if fields have expected values
		assertEquals(availableSlotsOnFriday.isEmpty(), true);
		
		// passing old date 
		List<AvailableSlotsDTO> availableSlotsInPast = appointmentService.getAvailableSlots("2003-07-26",2,2,null);

		assertNotNull(availableSlotsInPast);

		// check if fields have expected values
		assertEquals(availableSlotsInPast.isEmpty(), true);
	}
	
	@Test
	public void test_getAvailableSlotsWithStartTime_withInvalidDates() {
		// passing friday 
		List<AvailableSlotsDTO> availableSlotsOnFriday = appointmentService.getAvailableSlots("2024-07-26",2,2,"08:00");

		assertNotNull(availableSlotsOnFriday);

		// check if fields have expected values
		assertEquals(availableSlotsOnFriday.isEmpty(), true);
		
		// passing old date 
		List<AvailableSlotsDTO> availableSlotsInPast = appointmentService.getAvailableSlots("2003-07-26",2,2,"08:00");

		assertNotNull(availableSlotsInPast);

		// check if fields have expected values
		assertEquals(availableSlotsInPast.isEmpty(), true);
	}
	
	@Test
	public void test_getAvailableSlotsWithStartTime_withClosingTime() {
		TimeSlotEntity timeSlot = prepareTimeSlotEntity();
		
		// mocking timeslot repository
		when(timeSlotsRepository.findByStartTime(LocalTime.parse("20:00"))).thenReturn(timeSlot);
		
		List<AvailableSlotsDTO> availableSlotsAfterClosingTime = appointmentService.getAvailableSlots("2024-07-28",2,4,"20:00");

		assertNotNull(availableSlotsAfterClosingTime);

		// should not have any appointment since its closing time
		assertEquals(availableSlotsAfterClosingTime.isEmpty(), true);
		
	}
	
	private TimeSlotEntity prepareTimeSlotEntity() {
		TimeSlotEntity timeSlot = new TimeSlotEntity();
		
		timeSlot.setSlotId(23);
		timeSlot.setSlotName("20:00-20:30");
		timeSlot.setStartTime(LocalTime.parse("20:00"));
		timeSlot.setEndTime(LocalTime.parse("20:30"));
		
		return timeSlot;
	}
	

}
