export type Role = "CEO" | "HR" | "MANAGER" | "EMPLOYEE";

export type User = {
  id: number;
  username: string;
  email: string;
  role: Role;
  isActive: boolean;
  managerId?: number | null;
  managerName?: string | null;

  permissions: string[]; // ✅ ADD THIS
};
export type RoleLog = {
  id: number;
  userId: number;
  userName: string;
  oldRole: string;
  newRole: string;
  changedBy: number;
  changedByName: string;
  createdAt: string;
};

export type RoleLogsResponse = {
  data: RoleLog[];
  total: number;
  page: number;
  limit: number;
};