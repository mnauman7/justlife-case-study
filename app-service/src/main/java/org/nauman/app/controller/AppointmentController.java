package org.nauman.app.controller;

import java.util.List;

import org.nauman.app.model.AppointmentDTO;
import org.nauman.app.model.AvailableSlotsDTO;
import org.nauman.app.model.CreateAppointmentRequestDTO;
import org.nauman.app.model.UpdateAppointmentRequestDTO;
import org.nauman.app.model.UserAppointmentDTO;
import org.nauman.app.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Appointment controller class
 */
@RestController
@RequestMapping("/appointment")
public class AppointmentController {
	
	private AppointmentService appointmentService;
	
	@Autowired
	public AppointmentController(AppointmentService appointmentService) {
		this.appointmentService = appointmentService;
	}
	
	/**
	 * @param date defines which date to search available slots
	 * @param serviceHours defines what should be the duration be of each available slot
	 * @param numberOfProfessionals defines how many staff member should be available in each slot
	 * @param selectedStartTime which time to search available slots
	 * @return List<AvailableSlotsDTO>  list of available slots found with given filters
	 */
	@GetMapping("/available-slots")
	public List<AvailableSlotsDTO>  getAvailableSlots(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String date,
			@RequestParam(name = "service-hours", required = false) Integer serviceHours,
			@RequestParam(name = "professionals", required = false) Integer numberOfProfessionals,
			@RequestParam(name = "start-time", required = false) String selectedStartTime) {
		
		return appointmentService.getAvailableSlots(date, serviceHours, numberOfProfessionals, selectedStartTime);
	}
	
	/**
	 * @param date defines which date to search available slots
	 * @param appointmentId existing appointment id
	 * @return List<AvailableSlotsDTO>  list of available slots for existing appointment
	 */
	@GetMapping("{appointmentId}/available-slots")
	public List<AvailableSlotsDTO>  getAvailableSlotsForExistingAppointment(
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") String date,
			@PathVariable("appointmentId") Integer appointmentId) {
		
		return appointmentService.getAvailableSlotsForExistingAppointment(date, appointmentId);
	}
	
    /**
     * @param appointmentRequestDTO contains parameters which are used to generate new appointment
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createAppointment(@RequestBody CreateAppointmentRequestDTO appointmentRequestDTO) {
    	appointmentService.createAppointment(appointmentRequestDTO);
    }
    
    
	/**
	 * @param userId
	 * @return list of appointments booked by this user
	 */
	@GetMapping("/user/{userId}")
	public List<UserAppointmentDTO> getUserAppointments(@PathVariable("userId") Integer userId) {
		return appointmentService.getUserAppointments(userId);
	}
	
	/**
	 * @param appointmentId
	 * @return AppointmentDTO appointment details
	 */
	@GetMapping("/{appointmentId}")
	public AppointmentDTO getAppointmentById(@PathVariable("appointmentId") Integer appointmentId) {
		return appointmentService.getAppointmentById(appointmentId);
	}
	
	/**
	 * @param userId 
	 * @param userDTO
	 */
	@PutMapping("/{appointmentId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateAppointment(@PathVariable("appointmentId") Integer appointmentId,
			@RequestBody UpdateAppointmentRequestDTO appointmentRequestDTO) {
		appointmentService.updateAppointment(appointmentId, appointmentRequestDTO);
	}

}
