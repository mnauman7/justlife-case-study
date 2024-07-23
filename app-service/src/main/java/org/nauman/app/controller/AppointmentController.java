package org.nauman.app.controller;

import java.util.List;

import org.nauman.app.model.AvailableSlotsDTO;
import org.nauman.app.model.CreateAppointmentRequestDTO;
import org.nauman.app.model.UserDTO;
import org.nauman.app.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {
	
	private AppointmentService appointmentService;
	
	@Autowired
	public AppointmentController(AppointmentService appointmentService) {
		this.appointmentService = appointmentService;
	}
	
	@GetMapping("/available-slots")
	public List<AvailableSlotsDTO>  getAvailableSlots(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String date,
			@RequestParam(name = "service-hours", required = false) String serviceHours,
			@RequestParam(name = "professionals", required = false) String numberOfProfessionals) {
		
		return appointmentService.getAvailableSlots(date, serviceHours, numberOfProfessionals);
	}
	
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createAppointment(@RequestBody CreateAppointmentRequestDTO appointmentRequestDTO) {
    	appointmentService.createAppointment(appointmentRequestDTO);
    }

}
