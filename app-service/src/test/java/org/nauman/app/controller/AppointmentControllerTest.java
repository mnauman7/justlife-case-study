package org.nauman.app.controller;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.nauman.app.model.AvailableSlotsDTO;
import org.nauman.app.model.StaffBookingDTO;
import org.nauman.app.service.AppointmentService;
import org.nauman.app.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ComponentScan(basePackages = {"org.nauman"})
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AppointmentControllerTest {

	@Autowired
	MockMvc mvc;

	@MockBean
	private AppointmentService appointmentService;
	

	//@Test
	public void testAvailableSlotsApi() throws Exception {		
		when(appointmentService.getAvailableSlots("2024-07-26", 2, 2, "08:00")).thenReturn(prepareAvailableSlots());

		mvc.perform(get("/appointment/available-slots?date=2024-07-26"))
		.andExpect(status().isOk());
	}
	
	private List<AvailableSlotsDTO> prepareAvailableSlots() {
		StaffBookingDTO staffBookingDTO = new StaffBookingDTO();
		staffBookingDTO.setStaffId(1);
		staffBookingDTO.setFullName("Nauman Test");
		staffBookingDTO.setVehicleId(2);
		
		StaffBookingDTO staffBookingDTO2 = new StaffBookingDTO();
		staffBookingDTO.setStaffId(2);
		staffBookingDTO.setFullName("Ali Test");
		staffBookingDTO.setVehicleId(2);
		
		List<StaffBookingDTO> staffBookingDTOList = new ArrayList<>();
		staffBookingDTOList.add(staffBookingDTO);
		staffBookingDTOList.add(staffBookingDTO2);
		
		
		AvailableSlotsDTO availableSlotsDTO = new AvailableSlotsDTO();
		availableSlotsDTO.setDate("2024-07-26");
		availableSlotsDTO.setDuration(4);
		availableSlotsDTO.setStartingTime("08:00");
		availableSlotsDTO.setStartingTimeId(1);
		availableSlotsDTO.setAvailableStaff(staffBookingDTOList);
		
		List<AvailableSlotsDTO> availableSlotsDTOList = new ArrayList<>();
		availableSlotsDTOList.add(availableSlotsDTO);
		
		return availableSlotsDTOList;
	}

}
