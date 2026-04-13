import { useQuery } from "@tanstack/react-query";
import { getUserById } from "../api/userApi";
import type { User } from "../types";

export const useUserById = (
  id: number,
  options?: { enabled?: boolean }
) => {
  return useQuery<User>({
    queryKey: ["user", id],
    queryFn: () => getUserById(id),
    enabled: options?.enabled ?? !!id,
  });
};

