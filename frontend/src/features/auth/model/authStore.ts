import { create } from "zustand";

export type User = {
  id: number;
  username: string;
  email: string;
  role: string;
  isActive: boolean;
  managerId: number | null;
  managerName: string | null;
  permissions: string[];
};

type AuthState = {
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  setUser: (user: User | null) => void;
  setLoading: (loading: boolean) => void;
  logout: () => void;
};

export const useAuthStore = create<AuthState>((set) => ({
  user: null,
  isAuthenticated: false,
  isLoading: true,

  setUser: (user) =>
    set({
      user,
      isAuthenticated: !!user,
      isLoading: false,
    }),

  setLoading: (loading) =>
    set({
      isLoading: loading,
    }),

  logout: () =>
    set({
      user: null,
      isAuthenticated: false,
      isLoading: false,
    }),
}));

export const useHasRole = (role: string) =>
  useAuthStore((s) => s.user?.role === role);

export const useHasPermission = (permission: string) =>
  useAuthStore((s) => s.user?.permissions?.includes(permission) ?? false);