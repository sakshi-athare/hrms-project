import type { Role } from "../../users/types";

export type User = {
  id: number;
  username: string;
  email: string;
  role: Role;
  permissions: string[];
  isActive?: boolean;
  managerId?: number | null;
  managerName?: string | null;
};