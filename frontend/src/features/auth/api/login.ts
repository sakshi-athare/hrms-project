import { api } from "../../../shared/api/axios";

type LoginInput = {
  email: string;
  password: string;
};

export const loginApi = async (data: LoginInput) => {
  const res = await api.post("/auth/login", data);
  return res.data;
};