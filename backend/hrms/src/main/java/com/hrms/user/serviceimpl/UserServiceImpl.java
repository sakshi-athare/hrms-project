package com.hrms.user.serviceimpl;

import com.hrms.common.enums.Role;
import com.hrms.common.exception.*;
import com.hrms.security.permission.Permission;
import com.hrms.security.permission.PermissionService;
import com.hrms.security.permission.RolePermissionMapper;
import com.hrms.user.dto.*;
import com.hrms.user.entity.*;
import com.hrms.user.repository.*;
import com.hrms.user.service.UserService;

import org.springframework.data.domain.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import jakarta.persistence.criteria.Predicate;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final RoleLogRepository roleLogRepository;
	private final PasswordEncoder passwordEncoder;
	private final PermissionService permissionService;

	public UserServiceImpl(UserRepository userRepository, RoleLogRepository roleLogRepository,
			PasswordEncoder passwordEncoder, PermissionService permissionService) {

		this.userRepository = userRepository;
		this.roleLogRepository = roleLogRepository;
		this.passwordEncoder = passwordEncoder;
		this.permissionService = permissionService;
	}

	/* ================= CURRENT USER ================= */

	public User getCurrentUser() {
		var auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth == null || auth.getName() == null) {
			throw new UnauthorizedActionException("User not authenticated");
		}

		String email = auth.getName();

		return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
	}

	@Override
	public UserResponse getCurrentUserProfile() {
		return mapToResponse(getCurrentUser());
	}

	/* ================= STATUS ================= */

	@Override
	public void updateUserStatus(Long userId, Boolean isActive) {
		User user = getUserOrThrow(userId);
		user.setIsActive(isActive);
		userRepository.save(user);
	}

	@Override
	public void deactivateUser(Long userId) {
		updateUserStatus(userId, false);
	}

	/* ================= COMMON ================= */

	private User getUserOrThrow(Long id) {
		return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
	}

	/* ================= MAPPER ================= */

	private UserResponse mapToResponse(User user) {
		Long managerId = null;
		String managerName = null;

		if (user.getManager() != null) {
			managerId = user.getManager().getId();
			managerName = user.getManager().getUsername();
		}

		List<String> permissions = RolePermissionMapper.getPermissions(user.getRole()).stream().map(Permission::name)
				.toList();

		return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole(), user.getIsActive(),
				managerId, managerName, permissions);
	}

	/* ================= CREATE ================= */

	@Override
	public UserResponse createUser(CreateUserRequest request) {

		if (userRepository.existsByEmail(request.getEmail())) {
			throw new BadRequestException("Email already exists");
		}

		User manager = null;
		if (request.getManagerId() != null) {
			manager = getUserOrThrow(request.getManagerId());
		}

		validateHierarchy(request.getRole(), manager);

		User user = new User();
		user.setUsername(request.getUsername());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(request.getRole());
		user.setManager(manager);

		userRepository.save(user);
		return mapToResponse(user);
	}

	/* ================= ROLE HIERARCHY ================= */

	private void validateHierarchy(Role role, User manager) {
		if (role == Role.CEO) {
			throw new BadRequestException("Cannot create CEO");
		}
		if (role == Role.HR && (manager == null || manager.getRole() != Role.CEO)) {
			throw new BadRequestException("HR must report to CEO");
		}
		if (role == Role.MANAGER && (manager == null || manager.getRole() != Role.HR)) {
			throw new BadRequestException("Manager must report to HR");
		}
		if (role == Role.EMPLOYEE && (manager == null || manager.getRole() != Role.MANAGER)) {
			throw new BadRequestException("Employee must report to Manager");
		}
	}

	/* ================= UPDATE ================= */

	@Override
	public UserResponse updateUser(Long userId, UpdateUserRequest request) {

		User currentUser = getCurrentUser();
		User user = getUserOrThrow(userId);

		if (user.getRole() == Role.CEO) {
			throw new BadRequestException("Cannot modify CEO");
		}

		if (request.getUsername() != null) {
			user.setUsername(request.getUsername());
		}

		if (request.getEmail() != null) {
			if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
				throw new BadRequestException("Email already exists");
			}
			user.setEmail(request.getEmail());
		}

		if (request.getIsActive() != null) {
			permissionService.check(currentUser, Permission.USER_CHANGE_STATUS);
			user.setIsActive(request.getIsActive());
		}
		if (request.getManagerId() != null) {

			permissionService.check(currentUser, Permission.USER_ASSIGN_MANAGER);

			User manager = getUserOrThrow(request.getManagerId());

			if (!Boolean.TRUE.equals(manager.getIsActive())) {
				throw new BadRequestException("Manager must be active");
			}

			validateHierarchy(user.getRole(), manager);

			if (manager.getId().equals(user.getId())) {
				throw new BadRequestException("User cannot be their own manager");
			}

			user.setManager(manager);
		}
		userRepository.save(user);
		return mapToResponse(user);
	}

	/* ================= LIST ================= */

	@Override
	public Map<String, Object> getUsers(String search, String role, Boolean status, int page, int limit) {

		Pageable pageable = PageRequest.of(page - 1, limit);

		Page<User> pageResult = userRepository.findAll((root, query, cb) -> {

			List<Predicate> predicates = new ArrayList<>();

			// 🔍 SEARCH
			if (search != null && !search.isEmpty()) {
				String pattern = "%" + search.toLowerCase() + "%";

				predicates.add(cb.or(cb.like(cb.lower(root.get("username")), pattern),
						cb.like(cb.lower(root.get("email")), pattern)));
			}

			if (role != null && !role.isEmpty()) {
				predicates.add(cb.equal(root.get("role"), role));
			}

			if (status != null) {
				predicates.add(cb.equal(root.get("isActive"), status));
			}

			return cb.and(predicates.toArray(new Predicate[0]));

		}, pageable);

		List<UserResponse> data = pageResult.getContent().stream().map(this::mapToResponse).toList();

		return Map.of("data", data, "total", pageResult.getTotalElements(), "page", page, "limit", limit);
	}

	/* ================= ROLE CHANGE ================= */

	@Override
	@Transactional
	public void changeRole(Long userId, ChangeRoleRequest request) {

		User currentUser = getCurrentUser();

		permissionService.check(currentUser, Permission.ROLE_CHANGE);

		User user = getUserOrThrow(userId);

		validateHierarchy(request.getRole(), user.getManager());

		Role oldRole = user.getRole();
		user.setRole(request.getRole());
		userRepository.save(user);

		RoleLog log = new RoleLog();
		log.setUser(user);
		log.setOldRole(oldRole);
		log.setNewRole(request.getRole());
		log.setChangedBy(currentUser);

		roleLogRepository.save(log);
	}

	/* ================= ROLE LOG ================= */

	@Override
	public Map<String, Object> getRoleLogs(int page, int limit) {

		Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "createdAt") // 🔥 latest first
		);

		Page<RoleLog> logsPage = roleLogRepository.findAll(pageable);

		List<RoleLogResponse> logs = logsPage.getContent().stream()
				.map(log -> new RoleLogResponse(log.getId(), log.getUser().getId(), log.getUser().getUsername(),
						log.getOldRole(), log.getNewRole(), log.getChangedBy().getId(),
						log.getChangedBy().getUsername(), log.getCreatedAt()))
				.toList();

		return Map.of("data", logs, "total", logsPage.getTotalElements(), "page", page, "limit", limit);
	}
	/* ================= INTERNAL ================= */

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
	}

	@Override
	public UserResponse getUserById(Long id) {
		User user = getUserOrThrow(id);
		return mapToResponse(user);
	}

	@Override
	public List<RoleLogResponse> getLogsByUser(Long userId) {

		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

		List<RoleLog> logs = roleLogRepository.findByUserIdOrderByCreatedAtDesc(user.getId());

		return logs.stream()
				.map(log -> new RoleLogResponse(log.getId(), log.getUser().getId(), log.getUser().getUsername(),
						log.getOldRole(), log.getNewRole(), log.getChangedBy().getId(),
						log.getChangedBy().getUsername(), log.getCreatedAt()))
				.toList();
	}
}
