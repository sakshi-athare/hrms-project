import { useMutation } from "@tanstack/react-query";
import { useAuthStore } from "../model/authStore";
import { loginApi } from "../api/login";

type LoginInput = {
  email: string;
  password: string;
};

export const useLogin = () => {
  const setUser = useAuthStore((s) => s.setUser);

  return useMutation({
    mutationFn: (data: LoginInput) => loginApi(data),

   onSuccess: (res) => {
  console.log("LOGIN RESPONSE:", res);

  const data = res?.data; 

  if (!data) {
    console.error("No user data");
    return;
  }

  const user = {
    id: data.id,
    username: data.username,
    email: data.email,
    role: data.role,
    isActive: data.isActive,
    managerId: data.managerId ?? null,
    managerName: data.managerName ?? null,
    permissions: data.permissions ?? [],
  };

  // console.log("SETTING USER:", user);

  setUser(user);
}
  });
};