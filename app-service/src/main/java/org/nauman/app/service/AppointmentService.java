package org.nauman.app.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.nauman.app.jpa.entity.StaffEntity;
import org.nauman.app.jpa.entity.TimeSlotsEntity;
import org.nauman.app.jpa.repository.StaffRepository;
import org.nauman.app.jpa.repository.TimeSlotsRepository;
import org.nauman.app.model.AvailableSlotsDTO;
import org.nauman.app.model.CreateAppointmentRequestDTO;
import org.nauman.app.model.StaffDTO;
import org.nauman.app.model.UserViewDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.transaction.Transactional;

@Service
public class AppointmentService {
	
	private TimeSlotsRepository timeSlotsRepository;
	
	private StaffRepository staffRepository;
	
	private ModelMapper modelMapper;
	
	private final Logger logs = LoggerFactory.getLogger(AppointmentService.class);
	
	@Autowired
	public AppointmentService(TimeSlotsRepository timeSlotsRepository, StaffRepository staffRepository, ModelMapper modelMapper) {
		this.timeSlotsRepository = timeSlotsRepository;
		this.staffRepository = staffRepository;
		this.modelMapper = modelMapper;
	}
	
	public List<AvailableSlotsDTO> getAvailableSlots(String date, String serviceHours, String numberOfProfessionals) {
		
		List<AvailableSlotsDTO> availableSlots = new ArrayList<>();
		
		try {
			
			List<TimeSlotsEntity> timeSlots = timeSlotsRepository.findByIsActive(true);
			
			List<StaffEntity> availableStaff = staffRepository.findByIsActive(true);
			
			List<StaffDTO> availableStaffDTOList = modelMapper.map(availableStaff, new TypeToken<List<StaffDTO>>() {}.getType());
			
			for(TimeSlotsEntity slot: timeSlots) {
				AvailableSlotsDTO availableSlotsDTO = new AvailableSlotsDTO();
				availableSlotsDTO.setDate(date);
				availableSlotsDTO.setStartingTime(slot.getSlotName());
				availableSlotsDTO.setAvailableStaff(availableStaffDTOList);
				availableSlots.add(availableSlotsDTO);
			}
			
		} catch (Exception e) {
			logs.error("getAvailableSlots failed", e);
		}
		
		return availableSlots;
	}
	
	@Transactional
    public void createAppointment(CreateAppointmentRequestDTO bookingRequestDTO) {
		
		
    	
    }
	
}
