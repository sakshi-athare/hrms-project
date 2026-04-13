import { useQuery } from "@tanstack/react-query";
import { api } from "../../../shared/api/axios";

export const useDashboard = () => {
  return useQuery({
    queryKey: ["dashboard-summary"],
    queryFn: async () => {
      const res = await api.get("/dashboard/summary");

     
      if (!res.data?.success) {
        throw new Error(res.data?.message || "Failed to fetch dashboard");
      }

      return res.data.data;
    },

    staleTime: 1000 * 60 * 5, 
    retry: 1, 
  });
};