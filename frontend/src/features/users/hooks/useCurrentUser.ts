import { useQuery } from "@tanstack/react-query";
import { getCurrentUser } from "../api/userApi";
import type { User } from "../types";

export const useCurrentUser = () => {
  return useQuery<User>({
    queryKey: ["currentUser"],
    queryFn: getCurrentUser,
  });
};