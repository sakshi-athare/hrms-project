package com.hrms.holiday.repository;

import com.hrms.holiday.entity.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

    Optional<Holiday> findByDate(LocalDate date);

    boolean existsByDate(LocalDate date);

    List<Holiday> findByYear(Integer year);

	List<Holiday> findByDateBetween(LocalDate start, LocalDate end);
	
	List<Holiday> findByActiveTrueOrderByDateAsc();

	List<Holiday> findByYearAndActiveTrueOrderByDateAsc(Integer year);
   
}