package com.hrms.holiday.serviceimpl;

import com.hrms.common.exception.BadRequestException;
import com.hrms.common.exception.ResourceNotFoundException;
import com.hrms.holiday.dto.HolidayMapper;
import com.hrms.holiday.dto.HolidayRequest;
import com.hrms.holiday.dto.HolidayResponse;
import com.hrms.holiday.entity.Holiday;
import com.hrms.holiday.repository.HolidayRepository;
import com.hrms.holiday.service.HolidayService;
import com.hrms.user.entity.User;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HolidayServiceImpl implements HolidayService {

    private final HolidayRepository holidayRepository;

    @Override
    public HolidayResponse createHoliday(HolidayRequest request, User user) {

        LocalDate today = LocalDate.now();

        if (request.getDate().isBefore(today)) {
            throw new BadRequestException("Cannot create holiday in past");
        }

        if (holidayRepository.existsByDate(request.getDate())) {
            throw new BadRequestException("Holiday already exists for this date");
        }

        Holiday holiday = Holiday.builder()
                .name(request.getName())
                .date(request.getDate())
                .year(request.getDate().getYear())
                .description(request.getDescription())
                .type(request.getType())
                .optionalHoliday(request.getOptionalHoliday())
                .active(true)
                .build();

        holidayRepository.save(holiday);

        return HolidayMapper.toDto(holiday);
    }

    @Override
    public List<HolidayResponse> getAllHolidays() {

        return holidayRepository.findByActiveTrueOrderByDateAsc()
                .stream()
                .map(HolidayMapper::toDto)
                .toList();
    }

    @Override
    public List<HolidayResponse> getHolidaysByYear(Integer year) {

        return holidayRepository.findByYearAndActiveTrueOrderByDateAsc(year)
                .stream()
                .map(HolidayMapper::toDto)
                .toList();
    }

    @Override
    public HolidayResponse updateHoliday(Long id, HolidayRequest request, User user) {

        Holiday holiday = holidayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Holiday not found"));

        if (holiday.getDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Cannot update past holiday");
        }

        boolean exists = holidayRepository.existsByDate(request.getDate());

        if (exists && !holiday.getDate().equals(request.getDate())) {
            throw new BadRequestException("Another holiday already exists on this date");
        }

        holiday.setName(request.getName());
        holiday.setDate(request.getDate());
        holiday.setYear(request.getDate().getYear());
        holiday.setDescription(request.getDescription());
        holiday.setType(request.getType());
        holiday.setOptionalHoliday(request.getOptionalHoliday());

        holidayRepository.save(holiday);

        return HolidayMapper.toDto(holiday);
    }
    
    @Override
    public void deleteHoliday(Long id) {

        Holiday holiday = holidayRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Holiday not found"));

        if (holiday.getDate().isBefore(LocalDate.now())) {
            throw new BadRequestException("Cannot delete past holiday");
        }

        holiday.setActive(false);

        holidayRepository.save(holiday);
    }
    
    
    
    
}