package com.hrms.holiday.service;

import com.hrms.holiday.dto.HolidayRequest;
import com.hrms.holiday.dto.HolidayResponse;
import com.hrms.user.entity.User;

import java.util.List;

public interface HolidayService {


	HolidayResponse createHoliday(HolidayRequest request, User user);
	
    List<HolidayResponse> getAllHolidays();

    List<HolidayResponse> getHolidaysByYear(Integer year);

    HolidayResponse updateHoliday(Long id, HolidayRequest request, User user);
    
    void deleteHoliday(Long id);
}