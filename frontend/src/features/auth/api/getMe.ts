import { api } from "../../../shared/api/axios";
import type { User } from "../model/authStore";

export const getMe = async (): Promise<User> => {
  const res = await api.get("/users/me");

  console.log("ME RAW RESPONSE:", res.data); // 🔍 DEBUG

  const data = res.data.data ?? res.data; // ✅ HANDLE BOTH CASES

  if (!data) {
    throw new Error("No user data");
  }

  return {
    id: data.id,
    username: data.username,
    email: data.email,
    role: data.role,
    isActive: data.isActive,
    managerId: data.managerId ?? null,
    managerName: data.managerName ?? null,
    permissions: data.permissions ?? [],
  };
};