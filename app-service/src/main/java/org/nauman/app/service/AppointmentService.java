package org.nauman.app.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.nauman.app.enums.StaffOccupancyType;
import org.nauman.app.jpa.entity.AppointmentEntity;
import org.nauman.app.jpa.entity.AppointmentStaffEntity;
import org.nauman.app.jpa.entity.StaffEntity;
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
import org.nauman.app.model.AppointmentDTO;
import org.nauman.app.model.AvailableSlotsDTO;
import org.nauman.app.model.CreateAppointmentRequestDTO;
import org.nauman.app.model.StaffBookingDTO;
import org.nauman.app.model.StaffDTO;
import org.nauman.app.model.UpdateAppointmentRequestDTO;
import org.nauman.app.model.UserAppointmentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

/**
 * This class is used to book new appointments and to check available slots for appointments.
 */
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
	
	/**
	 * @param date defines which date to search available slots
	 * @param serviceHours defines what should be the duration be of each available slot
	 * @param numberOfProfessionals defines how many staff member should be available in each slot
	 * @param selectedStartTime which time to search available slots
	 * @return List<AvailableSlotsDTO>  list of available slots found with given filters
	 */
	public List<AvailableSlotsDTO> getAvailableSlots(String date, Integer serviceHours, Integer numberOfProfessionals,
			String selectedStartTime) {
		
		LocalDate selectedDate = LocalDate.parse(date);
		if (selectedDate.getDayOfWeek() == DayOfWeek.FRIDAY || selectedDate.isBefore(LocalDate.now())) {
			// no appointments available on friday or past dates
			return new ArrayList<>();
		}
		
		if (selectedStartTime != null && !selectedStartTime.isBlank()) {
			return getAvailableSlotsWithStartTime(date, serviceHours, numberOfProfessionals,
					selectedStartTime);
		} else {
			return getAllAvailableSlots(date, serviceHours, numberOfProfessionals);
		}
	}
	
	/**
	 * @param date defines which date to search available slots
	 * @param appointmentId
	 * @return List<AvailableSlotsDTO>  list of available slots found with given filters
	 */
	public List<AvailableSlotsDTO> getAvailableSlotsForExistingAppointment(String date, Integer appointmentId) {
		
		List<AvailableSlotsDTO> availableSlotsForAppointment = new ArrayList<>();
		
		try {
			
			LocalDate selectedDate = LocalDate.parse(date);
			
			if (selectedDate.getDayOfWeek() == DayOfWeek.FRIDAY || selectedDate.isBefore(LocalDate.now())) {
				// no appointments available on friday or past dates
				return new ArrayList<>();
			}
			
			Optional<AppointmentEntity> optionalAppointment = 
					appointmentRepository.findAppointmentWithStaffByAppointmentId(appointmentId);
			
			if(optionalAppointment.isEmpty()) {
				return new ArrayList<>();
			}
			
			AppointmentEntity appointmentEntity = optionalAppointment.get();
			
			AppointmentDTO appointmentDTO = getAppointmentDTOFromEntity(appointmentEntity);
			
			List<Integer> staffIds = getStaffIdsFromAppointmentDTO(appointmentDTO);
			
			List<TimeSlotEntity> availableSlots = timeSlotsRepository.findAvailableTimeSlotsForStaff(staffIds, selectedDate);
			
			for(TimeSlotEntity timeSlot : availableSlots ) {
				AvailableSlotsDTO availableSlotDTO = new AvailableSlotsDTO();
				availableSlotDTO.setDate(date);
				availableSlotDTO.setStartingTime(timeSlot.getSlotName());
				availableSlotDTO.setStartingTimeId(timeSlot.getSlotId());
				
				availableSlotsForAppointment.add(availableSlotDTO);
			}
			
		}catch (Exception e) {
			logs.error("getAvailableSlotsForExistingAppointment failed", e);
		}
		
		return availableSlotsForAppointment;
	}
	
	/**
	 * 
	 * Searches for time slot in selectedStartTime time only
	 * 
	 * @param date defines which date to search available slots
	 * @param serviceHours defines what should be the duration be of each available slot
	 * @param numberOfProfessionals defines how many staff member should be available in each slot
	 * @param selectedStartTime which time to search available slots
	 * @return List<AvailableSlotsDTO>  list of available slots found with given filters
	 */
	private List<AvailableSlotsDTO> getAvailableSlotsWithStartTime(String date, Integer serviceHours,
			Integer numberOfProfessionals, String selectedStartTime) {

		List<AvailableSlotsDTO> availableSlots = new ArrayList<>();

		try {

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
	
	
	/**
	 * Searches all time slots and returns available staff in each time slot
	 * 
	 * @param date defines which date to search available slots
	 * @param serviceHours defines what should be the duration be of each available slot
	 * @param numberOfProfessionals defines how many staff member should be available in each slot
	 * @return List<AvailableSlotsDTO>  list of available slots found with given filters
	 */
	private List<AvailableSlotsDTO> getAllAvailableSlots(String date, Integer serviceHours, Integer numberOfProfessionals) {
		
		List<AvailableSlotsDTO> availableSlots = new ArrayList<>();
		
		try {
			
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
	
	
	/**
	 * @param date
	 * @param timeSlot
	 * @param serviceHours
	 * @param numberOfProfessionals
	 * @return List<StaffBookingDTO> staff which are available for this time slot
	 */
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
	
	/**
	 * This method creates new appointments
	 * There are three steps when creating new appointment:
	 * 1). Create new entry in Appointment table
	 * 2). Store relation for staff and appointment in AppointmentStaff table
	 * 3). Update StaffOccupancy table to ensure that this staff is shown as unavailable in this time slot in booking section
	 * 
	 * @param appointmentRequestDTO contains parameters to book new appointment with
	 */
	@Transactional
    public void createAppointment(CreateAppointmentRequestDTO appointmentRequestDTO) {
		
		try {
			
			//saving appointment entry
			AppointmentEntity appointmentEntity = prepareAppointmentEntityFromRequest(appointmentRequestDTO);
			appointmentEntity = appointmentRepository.save(appointmentEntity);
			
			AppointmentDTO appointmentDTO = getAppointmentDTOFromEntity(appointmentEntity);
			
			//saving appointment staff relation
			List<AppointmentStaffEntity> appointmentStaffEntities = 
					prepareAppointmentStaffRelationEntities(appointmentDTO.getAppointmentId(), appointmentRequestDTO.getRequiredStaff());
 			
			appointmentStaffRepository.saveAll(appointmentStaffEntities);
			
			//add staff occupancy
			addStaffOccupancy(appointmentDTO, appointmentRequestDTO.getRequiredStaff());
			
		}catch(Exception e) {
			logs.error("createAppointment failed", e);
		}
    }
	
	
	/**
	 * This method updates existing appointments
	 * 
	 * @param appointmentRequestDTO contains parameters to update date and time of appointment
	 */
	@Transactional
    public void updateAppointment(Integer appointmentId, UpdateAppointmentRequestDTO appointmentRequestDTO) {
		
		try {
			Optional<AppointmentEntity> optionalAppointment = 
					appointmentRepository.findAppointmentWithStaffByAppointmentId(appointmentId);
			
			if(optionalAppointment.isEmpty()) {
				return;
			}
			
			AppointmentEntity appointmentEntity = optionalAppointment.get();
			
			AppointmentDTO appointmentDTO = getAppointmentDTOFromEntity(appointmentEntity);
			
			//updating appointment entry
			appointmentRepository.updateDateAndTimeOfAppointment(appointmentRequestDTO.getStartingTimeId(),
					appointmentRequestDTO.getAppointmentDate(), appointmentId);
			
			// free up time for this appointment so booking is available for this time slot now
			staffOccupancyRepository.deleteStaffOccupancyByAppointmentId(appointmentId);
			
			//add staff occupancy
			addStaffOccupancy(appointmentDTO, appointmentDTO.getStaff());
			
		}catch(Exception e) {
			logs.error("updateAppointment failed", e);
		}
    }
	
	
	/**
	 * Adds staff occupancy.
	 * It occupies staff by adding an entry in StaffOccupancy table.
	 * If staff does not contain entry for a time slot in StaffOccupancy table then that staff 
	 * is considered available for bookings in the slot.
	 * Updating staff occupancy ensures that staff is fully booked in this time slot
	 * and will not be taking new appointments in this slot.
	 * 
	 * @param appointmentRequestDTO
	 */
	private void addStaffOccupancy(AppointmentDTO appointmentDTO, List<StaffDTO> requiredStaffList) {
		
		List<StaffOccupancyEntity> staffOccupancyEntityList = new ArrayList<>();
		
		TimeSlotEntity startingTimeSlot = timeSlotsRepository.findById(appointmentDTO.getStartTimeSlotId()).get();
		 
		LocalTime startTime = startingTimeSlot.getStartTime();
		LocalTime endTime = startTime.plusHours(appointmentDTO.getDuration());
		
		List<TimeSlotIdView> workingTimeSlots = 
				timeSlotsRepository.findSlotIdsByStartTimeGreaterThanEqualAndStartTimeLessThanEqual(startTime, endTime);
		
		TimeSlotIdView breakTimeSlot = timeSlotsRepository.findSlotIdByStartTime(endTime.plusMinutes(30));
		
		for (StaffDTO requiredStaff : requiredStaffList) {
			// occupying slots for work
			for(TimeSlotIdView timeSlot: workingTimeSlots) {
				StaffOccupancyEntity staffOccupancyEntity = new StaffOccupancyEntity();
				staffOccupancyEntity.setStaffId(requiredStaff.getStaffId());
				staffOccupancyEntity.setTimeSlotId(timeSlot.getSlotId());
				staffOccupancyEntity.setOccupancyDate(appointmentDTO.getAppointmentDate());
				staffOccupancyEntity.setOccupancyTypeId(StaffOccupancyType.WORK.getValue());
				staffOccupancyEntity.setAppointmentId(appointmentDTO.getAppointmentId());
				
				staffOccupancyEntityList.add(staffOccupancyEntity);
			}
			
			if(breakTimeSlot == null) {
				continue;
			}
			
			// occupying slot for break
			StaffOccupancyEntity staffOccupancyEntity = new StaffOccupancyEntity();
			staffOccupancyEntity.setStaffId(requiredStaff.getStaffId());
			staffOccupancyEntity.setTimeSlotId(breakTimeSlot.getSlotId());
			staffOccupancyEntity.setOccupancyDate(appointmentDTO.getAppointmentDate());
			staffOccupancyEntity.setOccupancyTypeId(StaffOccupancyType.BREAK.getValue());
			staffOccupancyEntity.setAppointmentId(appointmentDTO.getAppointmentId());
			
			staffOccupancyEntityList.add(staffOccupancyEntity);
		}
		
		
		List<StaffIdView> allVehicleStaff =
				staffRepository.findStaffIdByVehicleIdAndIsActive(appointmentDTO.getVehicleId(), true);
		
		for(StaffIdView vehicleStaff : allVehicleStaff) {
			if(!listContainsId(requiredStaffList, vehicleStaff.getStaffId())) {
				
				// as vehicle will be busy driving required staff to location at starting time slot
				// setting other non working staff of the vehicle to occupied as vehicle is not available in this slot for them
				StaffOccupancyEntity staffOccupancyEntity = new StaffOccupancyEntity();
				staffOccupancyEntity.setStaffId(vehicleStaff.getStaffId());
				staffOccupancyEntity.setTimeSlotId(startingTimeSlot.getSlotId());
				staffOccupancyEntity.setOccupancyDate(appointmentDTO.getAppointmentDate());
				staffOccupancyEntity.setOccupancyTypeId(StaffOccupancyType.VEHICLE_BUSY.getValue());
				staffOccupancyEntity.setAppointmentId(appointmentDTO.getAppointmentId());
				
				staffOccupancyEntityList.add(staffOccupancyEntity);	
			}
		}
		
		if(!staffOccupancyEntityList.isEmpty()) {
			staffOccupancyRepository.saveAll(staffOccupancyEntityList);
		};
	}
	
	private boolean listContainsId(List<StaffDTO> staffList, Integer staffId) {
	    return staffList.stream().anyMatch(p -> p.getStaffId().equals(staffId));
	}

	/**
	 * Prepares AppointmentEntity which needs to be saved in database for appointment creation process.
	 * @param appointmentRequestDTO
	 * @return AppointmentEntity
	 */
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
	
	
	/**
	 * Prepares AppointmentStaffEntity which needs to be saved in database for appointment creation process.
	 * @param appointmentRequestDTO
	 * @param appointmentEntity
	 * @return List<AppointmentStaffEntity> 
	 */
	private List<AppointmentStaffEntity> prepareAppointmentStaffRelationEntities(Integer appointmentId,
			List<StaffDTO> requiredStaffList) {

		List<AppointmentStaffEntity> appointmentStaffEntities = new ArrayList<>();

		for (StaffDTO staff : requiredStaffList) {
			AppointmentStaffEntity appointmentStaffEntity = new AppointmentStaffEntity();
			appointmentStaffEntity.setAppointmentId(appointmentId);
			appointmentStaffEntity.setStaffId(staff.getStaffId());
			
			appointmentStaffEntities.add(appointmentStaffEntity);
		}

		return appointmentStaffEntities;
	}
	
	
	/**
	 * @param userId
	 * @return list of all appointments booked by this user
	 */
	public List<UserAppointmentDTO> getUserAppointments(Integer userId) {
		
		List<UserAppointmentDTO> userAppointments = new ArrayList<>();
		
		try {
			
			List<AppointmentEntity> userAppointmentsEntities =
					appointmentRepository.findAppointmentWithTimeSlotByUserId(userId);
			
			userAppointments = getUserAppointmentDTOFromEntity(userAppointmentsEntities);
			
			return userAppointments;
			
		}catch(Exception e) {
			logs.error("getUserAppointments failed", e);
		}
		
		
		return userAppointments;
		
	}
	
	/**
	 * @param userId
	 * @return list of all appointments booked by this user
	 */
	public AppointmentDTO getAppointmentById(Integer appointmentId) {
				
		try {
			
			Optional<AppointmentEntity> optionalAppointment =
					appointmentRepository.findAppointmentWithStaffByAppointmentId(appointmentId);
			
			if(optionalAppointment.isEmpty()) {
				return null;
			}
			
			AppointmentDTO appointment = getAppointmentDTOFromEntity(optionalAppointment.get());
			
			return appointment;
			
		}catch(Exception e) {
			logs.error("getUserAppointments failed", e);
		}
		
		
		return null;
	}
	
	private List<UserAppointmentDTO> getUserAppointmentDTOFromEntity(List<AppointmentEntity> userAppointmentsEntities){
		
		List<UserAppointmentDTO> userAppointmentsDTO = new ArrayList<>();
		
		for(AppointmentEntity appointmentEntity : userAppointmentsEntities) {
			
			UserAppointmentDTO userAppointment = new UserAppointmentDTO();
			userAppointment.setAppointmentId(appointmentEntity.getAppointmentId());
			userAppointment.setAddress(appointmentEntity.getAddress());
			userAppointment.setAppointmentDate(appointmentEntity.getAppointmentDate());
			userAppointment.setCity(appointmentEntity.getCity());
			userAppointment.setCreatedDate(appointmentEntity.getCreatedDate());
			userAppointment.setDuration(appointmentEntity.getDuration());
			userAppointment.setServiceType(appointmentEntity.getServiceType());
			userAppointment.setUpdatedDate(appointmentEntity.getUpdatedDate());
			userAppointment.setUserId(appointmentEntity.getUserId());
			userAppointment.setVehicleId(appointmentEntity.getVehicleId());
			
			if(appointmentEntity.getTimeSlots() != null) {
				userAppointment.setStartTime(appointmentEntity.getTimeSlots().getSlotName());
			}
			
			userAppointmentsDTO.add(userAppointment);
		}
		
		return userAppointmentsDTO;
	}
	
	/**
	 * Converts appointmentEntity to appointmentDTO
	 * Also converts nested List<StaffEntity> to List<StaffDTO>
	 * Creating DTO manually because model mapper is failing to convert nested object
	 * 
	 * @param appointmentEntity
	 * @return AppointmentDTO
	 */
	private AppointmentDTO getAppointmentDTOFromEntity(AppointmentEntity appointmentEntity){
		
		AppointmentDTO appointmentDTO = new AppointmentDTO();
			
		appointmentDTO.setAppointmentId(appointmentEntity.getAppointmentId());
		appointmentDTO.setAddress(appointmentEntity.getAddress());
		appointmentDTO.setAppointmentDate(appointmentEntity.getAppointmentDate());
		appointmentDTO.setCity(appointmentEntity.getCity());
		appointmentDTO.setCreatedDate(appointmentEntity.getCreatedDate());
		appointmentDTO.setDuration(appointmentEntity.getDuration());
		appointmentDTO.setServiceType(appointmentEntity.getServiceType());
		appointmentDTO.setStartTimeSlotId(appointmentEntity.getStartTimeSlotId());
		appointmentDTO.setUpdatedDate(appointmentEntity.getUpdatedDate());
		appointmentDTO.setUserId(appointmentEntity.getUserId());
		appointmentDTO.setVehicleId(appointmentEntity.getVehicleId());
		
		if(appointmentEntity.getStaff() == null) {
			return appointmentDTO;
		}
		
		List<StaffDTO> staffDTOList = new ArrayList<>();
		
		for(StaffEntity staffEntity : appointmentEntity.getStaff()) {
			
			StaffDTO staffDTO = this.modelMapper.map(staffEntity, StaffDTO.class);
			staffDTOList.add(staffDTO);
		}
		
		appointmentDTO.setStaff(staffDTOList);
		
		return appointmentDTO;
	}
	
	
	private List<Integer> getStaffIdsFromAppointmentDTO(AppointmentDTO appointmentDTO){
		List<Integer> staffIds = new ArrayList<>();
		
		List<StaffDTO> staffList = appointmentDTO.getStaff();
		
		for(StaffDTO staff : staffList) {
			staffIds.add(staff.getStaffId());
		}
		
		return staffIds;
	}
}
