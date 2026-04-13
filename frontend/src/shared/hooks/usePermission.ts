import { useAuthStore } from "../../features/auth/model/authStore";

export const usePermission = () => {
  const permissions = useAuthStore((s) => s.user?.permissions);

  const has = (perm?: string | string[]) => {
    // allow render until loaded
    if (!permissions || permissions.length === 0) return true;

    if (!perm) return true;

    if (Array.isArray(perm)) {
      return perm.some((p) => permissions.includes(p));
    }

    return permissions.includes(perm);
  };

  return { has };
};