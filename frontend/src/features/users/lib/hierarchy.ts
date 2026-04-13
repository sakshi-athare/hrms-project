import type { User } from "../types";

export const getAllowedManagers = (
  role: string,
  users: User[]
) => {
  if (role === "EMPLOYEE") {
    return users.filter((u) => u.role === "MANAGER");
  }

  if (role === "MANAGER") {
    return users.filter((u) => u.role === "HR");
  }

  if (role === "HR") {
    return users.filter((u) => u.role === "CEO");
  }

  return [];
};