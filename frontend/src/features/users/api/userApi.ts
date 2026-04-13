import { api } from "../../../shared/api/axios";
import type { RoleLogsResponse, User } from "../types";

/* ================= TYPES ================= */

export type UsersResponse = {
  data: User[];
  total: number;
  page: number;
  limit: number;
};

/* ================= USERS ================= */

// ✅ NORMALIZED RESPONSE (VERY IMPORTANT)
export const getUsers = async (params: {
  search?: string;
  page: number;
  limit: number;
  role?: string;
  status?: string | boolean;
  sortBy?: string;
  sortOrder?: string;
}): Promise<UsersResponse> => {
  const res = await api.get("/users", { params });

  console.log("USERS RAW:", res.data); // debug

  const payload = res.data?.data;

  return {
    data: payload?.data || payload?.content || [],
    total: payload?.total || 0,
    page: payload?.page || 1,
    limit: payload?.limit || 10,
  };
};

// GET USER BY ID
export const getUserById = async (id: number): Promise<User> => {
  const res = await api.get(`/users/${id}`);
  return res.data.data;
};

// CREATE USER
export const createUser = async (data: {
  username: string;
  email: string;
  password: string;
  role: string;
  managerId?: number;
}) => {
  const res = await api.post("/users", data);
  return res.data.data;
};

// UPDATE USER
export const updateUser = async (
  id: number,
  data: {
    username?: string;
    email?: string;
    isActive?: boolean;
    managerId?: number;
  }
) => {
  const res = await api.patch(`/users/${id}`, data);
  return res.data.data;
};

// STATUS
// userApi.ts

export const updateUserStatus = async ({
  userId,
  isActive,
}: {
  userId: number;
  isActive: boolean;
}) => {
  return api.patch(`/users/${userId}/status`, { isActive });
};

// ROLE CHANGE
export const changeRole = async ({
  userId,
  role,
}: {
  userId: number;
  role: string;
}) => {
  return api.patch(`/users/${userId}/role`, { role });
};

// CURRENT USER
export const getCurrentUser = async (): Promise<User> => {
  const res = await api.get("/users/me");
  return res.data.data ?? res.data;
};

// ROLE LOGS
export const getRoleLogs = async (params: {
  page: number;
  limit: number;
}): Promise<RoleLogsResponse> => {
  const res = await api.get("/users/role-logs", { params });

  if (!res.data || !res.data.data) {
    throw new Error("Invalid role logs response");
  }

  return res.data.data;
};






