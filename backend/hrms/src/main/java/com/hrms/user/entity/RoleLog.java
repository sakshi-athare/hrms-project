package com.hrms.user.entity;

import com.hrms.common.entity.BaseEntity;
import com.hrms.common.enums.Role;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role_logs")
public class RoleLog extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private User user;

	@Enumerated(EnumType.STRING)
	private Role oldRole;

	@Enumerated(EnumType.STRING)
	private Role newRole;

	@ManyToOne
	private User changedBy;

	@Column(length = 255)
	private String reason;

}