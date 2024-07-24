package org.nauman.app.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.nauman.app.enums.StaffOccupancyType;
import org.nauman.app.jpa.entity.AppointmentEntity;
import org.nauman.app.jpa.entity.AppointmentStaffEntity;
import org.nauman.app.jpa.entity.StaffOccupancyEntity;
import org.nauman.app.jpa.entity.TimeSlotEntity;
import org.nauman.app.jpa.projections.StaffBookingView;
import org.nauman.app.jpa.projections.StaffIdView;
import org.nauman.app.jpa.projections.TimeSlotIdView;
import org.nauman.app.jpa.repository.AppointmentRepository;
import org.nauman.app.jpa.repository.AppointmentStaffRepository;
import org.nauman.app.jpa.repository.StaffOccupancyRepository;
import org.nauman.app.jpa.repository.StaffRepository;
import org.nauman.app.jpa.repository.TimeSlotsRepository;
import org.nauman.app.model.AvailableSlotsDTO;
import org.nauman.app.model.CreateAppointmentRequestDTO;
import org.nauman.app.model.StaffBookingDTO;
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
	
	private static LocalTime CLOSING_TIME = LocalTime.parse("22:00");
	
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
	
	public List<AvailableSlotsDTO> getAvailableSlots(String date, Integer serviceHours, Integer numberOfProfessionals,
			String selectedStartTime) {
		
		if (selectedStartTime != null && !selectedStartTime.isBlank()) {
			return getAvailableSlotsWithStartTime(date, serviceHours, numberOfProfessionals,
					selectedStartTime);
		} else {
			return getAllAvailableSlots( date, serviceHours, numberOfProfessionals);
		}
		
	}
	
	public List<AvailableSlotsDTO> getAvailableSlotsWithStartTime(String date, Integer serviceHours,
			Integer numberOfProfessionals, String selectedStartTime) {

		List<AvailableSlotsDTO> availableSlots = new ArrayList<>();

		try {

			LocalDate localDate = LocalDate.parse(date);
			if (localDate.getDayOfWeek() == DayOfWeek.FRIDAY) {
				// no appointments on friday
				return new ArrayList<>();
			}

			LocalTime startTime = LocalTime.parse(selectedStartTime);

			TimeSlotEntity timeSlot = timeSlotsRepository.findByStartTime(startTime);

			if (timeSlot.getEndTime().plusHours(serviceHours).isAfter(CLOSING_TIME)) {
				// skip if time slot exceed closing time
				return availableSlots;
			}

			AvailableSlotsDTO availableSlotDTO = new AvailableSlotsDTO();
			availableSlotDTO.setDate(date);
			availableSlotDTO.setStartingTime(timeSlot.getSlotName());
			availableSlotDTO.setStartingTimeId(timeSlot.getSlotId());
			availableSlotDTO.setDuration(serviceHours);

			availableSlotDTO.setAvailableStaff(
					getAvailableStaffForThisSlot(date, timeSlot, serviceHours, numberOfProfessionals));

			if (availableSlotDTO.getAvailableStaff() != null && !availableSlotDTO.getAvailableStaff().isEmpty()) {

				availableSlots.add(availableSlotDTO);
			}

		} catch (Exception e) {
			logs.error("getAvailableSlots failed", e);
		}

		return availableSlots;
	}
	
	
	public List<AvailableSlotsDTO> getAllAvailableSlots(String date, Integer serviceHours, Integer numberOfProfessionals) {
		
		List<AvailableSlotsDTO> availableSlots = new ArrayList<>();
		
		try {
			
			LocalDate localDate = LocalDate.parse(date);
			if(localDate.getDayOfWeek() == DayOfWeek.FRIDAY) {
				// no appointments on friday
				return new ArrayList<>();
			}
			
			List<TimeSlotEntity> timeSlots = timeSlotsRepository.findAll();
			
			for (TimeSlotEntity slot: timeSlots) {
				if(slot.getEndTime().plusHours(serviceHours).isAfter(CLOSING_TIME)) {
					//skip if time slot exceed closing time
					continue;
				}
				
				AvailableSlotsDTO availableSlotDTO = new AvailableSlotsDTO();
				availableSlotDTO.setDate(date);
				availableSlotDTO.setStartingTime(slot.getSlotName());
				availableSlotDTO.setStartingTimeId(slot.getSlotId());
				availableSlotDTO.setDuration(serviceHours);
				
				availableSlotDTO.setAvailableStaff(getAvailableStaffForThisSlot(date, slot,
						serviceHours,  numberOfProfessionals));
				
				
				if (availableSlotDTO.getAvailableStaff() != null 
						&& !availableSlotDTO.getAvailableStaff().isEmpty()) {
					
					availableSlots.add(availableSlotDTO);
				}
			}
			
		} catch (Exception e) {
			logs.error("getAvailableSlots failed", e);
		}
		
		return availableSlots;
	}
	
	
	private List<StaffBookingDTO> getAvailableStaffForThisSlot(String date, TimeSlotEntity timeSlot,
			Integer serviceHours, Integer numberOfProfessionals) {
		
		List<StaffBookingDTO> staffBookingDTOList = new ArrayList<>();
		
		List<StaffBookingView> totalAvailableStaff = 
				staffRepository.
				findAvailableStaffForBookingSlot(date, timeSlot.getStartTime(),
						timeSlot.getStartTime().plusHours(serviceHours));
		
		//if vehicle does not have number of professionals required then remove that vehicle staff from the list
		List<StaffBookingView> availableStaffWithFilter = totalAvailableStaff.stream()
				.collect(Collectors.groupingBy(StaffBookingView::getVehicleId))
		        .entrySet().stream().filter(e -> e.getValue().size() >= numberOfProfessionals)
		        .flatMap(e -> e.getValue().stream()).toList();
		
		
		for (StaffBookingView staffBookingView : availableStaffWithFilter) {
			StaffBookingDTO staffBookingDTO = new StaffBookingDTO();
			staffBookingDTO.setStaffId(staffBookingView.getStaffId());
			staffBookingDTO.setFullName(staffBookingView.getFirstName() + " " + staffBookingView.getLastName());
			staffBookingDTO.setVehicleId(staffBookingView.getVehicleId());
			
			staffBookingDTOList.add(staffBookingDTO);
		}
		
		return staffBookingDTOList;
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
			logs.error("createAppointment failed", e);
		}
    }
	
	
	private void updateStaffOccupancy(CreateAppointmentRequestDTO appointmentRequestDTO) {
		
		List<StaffOccupancyEntity> staffOccupancyEntityList = new ArrayList<>();
		
		TimeSlotEntity startingTimeSlot = timeSlotsRepository.findById(appointmentRequestDTO.getStartingTimeId()).get();
		 
		LocalTime startTime = startingTimeSlot.getStartTime();
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
				// setting other non working staff of the vehicle to occupied as vehicle is not available in this slot for them
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
		appointmentEntity.setStartTimeSlotId(appointmentRequestDTO.getStartingTimeId());
		appointmentEntity.setDuration(appointmentRequestDTO.getDuration());
		appointmentEntity.setServiceType(appointmentRequestDTO.getServiceTypeId());
		appointmentEntity.setVehicleId(appointmentRequestDTO.getVehicleId());
		appointmentEntity.setAddress(appointmentRequestDTO.getAddress());
		appointmentEntity.setCity(appointmentRequestDTO.getCity());
		appointmentEntity.setAppointmentDate(appointmentRequestDTO.getAppointmentDate());
		appointmentEntity.setCreatedDate(LocalDateTime.now());
		
		return appointmentEntity;
	}
	
	
	private List<AppointmentStaffEntity> prepareAppointmentStaffRelationEntities(
			CreateAppointmentRequestDTO appointmentRequestDTO, AppointmentEntity appointmentEntity) {

		List<AppointmentStaffEntity> appointmentStaffEntities = new ArrayList<>();

		for (StaffDTO staff : appointmentRequestDTO.getRequiredStaff()) {
			AppointmentStaffEntity appointmentStaffEntity = new AppointmentStaffEntity();
			appointmentStaffEntity.setAppointmentId(appointmentEntity.getAppointmentId());
			appointmentStaffEntity.setStaffId(staff.getStaffId());
			
			appointmentStaffEntities.add(appointmentStaffEntity);
		}

		return appointmentStaffEntities;
	}
	
}
