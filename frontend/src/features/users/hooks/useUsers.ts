import { useQuery } from "@tanstack/react-query";
import { getUsers, type UsersResponse } from "../api/userApi";

export const useUsers = (params: {
  search?: string;
  page: number;
  limit: number;
  role?: string;
  status?: boolean;
  sortBy?: string;
  sortOrder?: string;
}) => {
  return useQuery<UsersResponse>({
    queryKey: [
      "users",
      params.search,
      params.page,
      params.limit,
      params.role,
      params.status,
      params.sortBy,
      params.sortOrder,
    ],

    queryFn: () => getUsers(params),

    placeholderData: (prev) => prev,
  });
};