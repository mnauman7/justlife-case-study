package org.nauman.app.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.nauman.app.enums.StaffOccupancyType;
import org.nauman.app.jpa.entity.AppointmentEntity;
import org.nauman.app.jpa.entity.AppointmentStaffEntity;
import org.nauman.app.jpa.entity.StaffEntity;
import org.nauman.app.jpa.entity.StaffOccupancyEntity;
import org.nauman.app.jpa.entity.TimeSlotEntity;
import org.nauman.app.jpa.projections.StaffIdView;
import org.nauman.app.jpa.projections.TimeSlotIdView;
import org.nauman.app.jpa.repository.AppointmentRepository;
import org.nauman.app.jpa.repository.AppointmentStaffRepository;
import org.nauman.app.jpa.repository.StaffOccupancyRepository;
import org.nauman.app.jpa.repository.StaffRepository;
import org.nauman.app.jpa.repository.TimeSlotsRepository;
import org.nauman.app.model.AvailableSlotsDTO;
import org.nauman.app.model.CreateAppointmentRequestDTO;
import org.nauman.app.model.StaffDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class AppointmentService {
	
	private AppointmentRepository appointmentRepository;
	
	private AppointmentStaffRepository appointmentStaffRepository;
	
	private TimeSlotsRepository timeSlotsRepository;
	
	private StaffRepository staffRepository;
	
	private StaffOccupancyRepository staffOccupancyRepository;
	
	private ModelMapper modelMapper;
	
	private final Logger logs = LoggerFactory.getLogger(AppointmentService.class);
	
	@Autowired
	public AppointmentService(TimeSlotsRepository timeSlotsRepository, StaffRepository staffRepository,
			AppointmentRepository appointmentRepository, AppointmentStaffRepository appointmentStaffRepository,
			StaffOccupancyRepository staffOccupancyRepository, ModelMapper modelMapper) {
		
		this.timeSlotsRepository = timeSlotsRepository;
		this.appointmentRepository = appointmentRepository;
		this.staffRepository = staffRepository;
		this.appointmentStaffRepository = appointmentStaffRepository;
		this.staffOccupancyRepository = staffOccupancyRepository;
		this.modelMapper = modelMapper;
	}
	
	public List<AvailableSlotsDTO> getAvailableSlots(String date, String serviceHours, String numberOfProfessionals) {
		
		List<AvailableSlotsDTO> availableSlots = new ArrayList<>();
		
		try {
			
			List<TimeSlotEntity> timeSlots = timeSlotsRepository.findAll();
			
			List<StaffEntity> availableStaff = staffRepository.findByIsActive(true);
			
			List<StaffDTO> availableStaffDTOList = modelMapper.map(availableStaff, new TypeToken<List<StaffDTO>>() {}.getType());
			
			for(TimeSlotEntity slot: timeSlots) {
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
    public void createAppointment(CreateAppointmentRequestDTO appointmentRequestDTO) {
		
		try {
			
			//saving appointment entry
			AppointmentEntity appointmentEntity = prepareAppointmentEntityFromRequest(appointmentRequestDTO);
			appointmentEntity = appointmentRepository.save(appointmentEntity);
			
			//saving appointment staff relation
			List<AppointmentStaffEntity> appointmentStaffEntities = 
					prepareAppointmentStaffRelationEntities(appointmentRequestDTO, appointmentEntity);
 			
			appointmentStaffRepository.saveAll(appointmentStaffEntities);
			
			//update staff occupancy
			updateStaffOccupancy(appointmentRequestDTO);
			
		}catch(Exception e) {
		}
    }
	
	
	private void updateStaffOccupancy(CreateAppointmentRequestDTO appointmentRequestDTO) {
		
		List<StaffOccupancyEntity> staffOccupancyEntityList = new ArrayList<>();
		
		TimeSlotEntity startingTimeSlot = timeSlotsRepository.findById(appointmentRequestDTO.getStartingTimeId()).get();
		 
		LocalTime startTime = startingTimeSlot.getEndTime();
		LocalTime endTime = startTime.plusHours(appointmentRequestDTO.getDuration());
		
		List<TimeSlotIdView> workingTimeSlots = 
				timeSlotsRepository.findSlotIdsByStartTimeGreaterThanEqualAndStartTimeLessThanEqual(startTime, endTime);
		
		TimeSlotIdView breakTimeSlot = timeSlotsRepository.findSlotIdByStartTime(endTime.plusMinutes(30));
		
		for (StaffDTO requiredStaff : appointmentRequestDTO.getRequiredStaff()) {
			// occupying slots for work
			for(TimeSlotIdView timeSlot: workingTimeSlots) {
				StaffOccupancyEntity staffOccupancyEntity = new StaffOccupancyEntity();
				staffOccupancyEntity.setStaffId(requiredStaff.getStaffId());
				staffOccupancyEntity.setTimeSlotId(timeSlot.getSlotId());
				staffOccupancyEntity.setOccupancyDate(appointmentRequestDTO.getAppointmentDate());
				staffOccupancyEntity.setOccupancyTypeId(StaffOccupancyType.WORK.getValue());
				
				staffOccupancyEntityList.add(staffOccupancyEntity);
			}
			
			// occupying slot for break
			StaffOccupancyEntity staffOccupancyEntity = new StaffOccupancyEntity();
			staffOccupancyEntity.setStaffId(requiredStaff.getStaffId());
			staffOccupancyEntity.setTimeSlotId(breakTimeSlot.getSlotId());
			staffOccupancyEntity.setOccupancyDate(appointmentRequestDTO.getAppointmentDate());
			staffOccupancyEntity.setOccupancyTypeId(StaffOccupancyType.BREAK.getValue());
			
			staffOccupancyEntityList.add(staffOccupancyEntity);
		}
		
		
		List<StaffIdView> allVehicleStaff =
				staffRepository.findStaffIdByVehicleIdAndIsActive(appointmentRequestDTO.getVehicleId(), true);
		
		for(StaffIdView vehicleStaff : allVehicleStaff) {
			if(!containsId(appointmentRequestDTO.getRequiredStaff(), vehicleStaff.getStaffId())) {
				
				// as vehicle will be busy driving required staff to location at starting time slot
				// setting other non working staff of the vehicle to also occupied as vehicle is not available in this slot 
				StaffOccupancyEntity staffOccupancyEntity = new StaffOccupancyEntity();
				staffOccupancyEntity.setStaffId(vehicleStaff.getStaffId());
				staffOccupancyEntity.setTimeSlotId(startingTimeSlot.getSlotId());
				staffOccupancyEntity.setOccupancyDate(appointmentRequestDTO.getAppointmentDate());
				staffOccupancyEntity.setOccupancyTypeId(StaffOccupancyType.VEHICLE_BUSY.getValue());
				
				staffOccupancyEntityList.add(staffOccupancyEntity);
				
			}
		}
		
		if(!staffOccupancyEntityList.isEmpty()) {
			staffOccupancyRepository.saveAll(staffOccupancyEntityList);
		};
	}
	
	private boolean containsId(List<StaffDTO> staffList, Integer staffId) {
	    return staffList.stream().anyMatch(p -> p.getStaffId().equals(staffId));
	}

	private AppointmentEntity prepareAppointmentEntityFromRequest(CreateAppointmentRequestDTO appointmentRequestDTO) {
		AppointmentEntity appointmentEntity = new AppointmentEntity();
		appointmentEntity.setUserId(appointmentRequestDTO.getUserId());
		appointmentEntity.setStartingTimeId(appointmentRequestDTO.getStartingTimeId());
		appointmentEntity.setDuration(appointmentRequestDTO.getDuration());
		appointmentEntity.setServiceType(appointmentRequestDTO.getServiceTypeId());
		appointmentEntity.setVehicleId(appointmentRequestDTO.getVehicleId());
		appointmentEntity.setAddress(appointmentRequestDTO.getAddress());
		appointmentEntity.setCity(appointmentRequestDTO.getCity());
		appointmentEntity.setAppointmentDate(appointmentRequestDTO.getAppointmentDate());
		
		return appointmentEntity;
	}
	
	
	private List<AppointmentStaffEntity> prepareAppointmentStaffRelationEntities(
			CreateAppointmentRequestDTO appointmentRequestDTO, AppointmentEntity appointmentEntity) {

		List<AppointmentStaffEntity> appointmentStaffEntities = new ArrayList<>();

		for (StaffDTO staff : appointmentRequestDTO.getRequiredStaff()) {
			AppointmentStaffEntity appointmentStaffEntity = new AppointmentStaffEntity();
			appointmentStaffEntity.setAppointmentId(appointmentEntity.getAppointmentId());
			appointmentStaffEntity.setStaffId(staff.getStaffId());
		}

		return appointmentStaffEntities;
	}
	
}
