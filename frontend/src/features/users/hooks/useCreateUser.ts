import { useMutation, useQueryClient } from "@tanstack/react-query";
import { createUser } from "../api/userApi";
import { useNavigate } from "react-router-dom";
import toast from "react-hot-toast";

export const useCreateUser = () => {
  const queryClient = useQueryClient();
  const navigate = useNavigate();

  return useMutation({
    mutationFn: createUser,

    onSuccess: () => {
      toast.success("User created");
      queryClient.invalidateQueries({ queryKey: ["users"] });
      navigate("/employees");
    },

    onError: (err: any) => {
      toast.error(err?.response?.data?.message || "Error");
    },
  });
};