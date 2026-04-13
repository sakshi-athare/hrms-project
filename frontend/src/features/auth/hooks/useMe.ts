import { useQuery } from "@tanstack/react-query";
import { getMe } from "../api/getMe";

export const useMe = () => {
  return useQuery({
    queryKey: ["me"],
    queryFn: getMe,
    retry: false,
  });
};