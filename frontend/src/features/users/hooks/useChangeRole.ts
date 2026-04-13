import { useMutation, useQueryClient } from "@tanstack/react-query";
import { changeRole } from "../api/userApi";
import toast from "react-hot-toast";

export const useChangeRole = () => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: changeRole,

    onSuccess: (_, variables) => {
      toast.success("Role updated");


      queryClient.invalidateQueries({ queryKey: ["users"] });


      queryClient.invalidateQueries({
        queryKey: ["user", variables.userId],
      });
    },

    onError: (err: any) => {
      toast.error(err?.response?.data?.message || "Error");
    },
  });
};