import { useMutation, useQueryClient } from "@tanstack/react-query";
import { updateUserStatus } from "../api/userApi";
import toast from "react-hot-toast";

export const useUpdateUserStatus = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: updateUserStatus,

    onSuccess: () => {
      toast.success("Status updated");

      
      queryClient.invalidateQueries({
        queryKey: ["users"],
        exact: false,
      });
    },

    onError: (err: any) => {
      toast.error(err?.response?.data?.message || "Error updating status");
    },
  });
};