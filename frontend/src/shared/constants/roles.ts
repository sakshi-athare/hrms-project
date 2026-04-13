export const ROLES = {
  CEO: "CEO",
  HR: "HR",
  MANAGER: "MANAGER",
  EMPLOYEE: "EMPLOYEE",
} as const;

export type Role = keyof typeof ROLES;