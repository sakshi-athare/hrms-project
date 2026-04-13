package com.hrms.attendance.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hrms.attendance.entity.Attendance;
import com.hrms.common.enums.AttendanceStatus;
import com.hrms.user.entity.User;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByUserAndDate(User user, LocalDate date);

    List<Attendance> findByUserOrderByDateDesc(User user);
    
    List<Attendance> findByUserAndDateBetween(User user, LocalDate start, LocalDate end);

	List<Attendance> findAllByOrderByDateDesc();
	
	@Query("SELECT a FROM Attendance a JOIN FETCH a.user WHERE a.user = :user ORDER BY a.date DESC")
	List<Attendance> findByUserWithUser(User user);
	
	@Query("""
			SELECT a FROM Attendance a
			JOIN FETCH a.user u
			WHERE 
			(:search IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%'))
			 OR LOWER(u.email) LIKE LOWER(CONCAT('%', :search, '%')))
			AND (:status IS NULL OR a.status = :status)
			AND (:startDate IS NULL OR a.date >= :startDate)
			AND (:endDate IS NULL OR a.date <= :endDate)
			""")
			Page<Attendance> findFilteredAttendance(
			        String search,
			        AttendanceStatus status,
			        LocalDate startDate,
			        LocalDate endDate,
			        Pageable pageable
			);

}