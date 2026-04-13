import axios from "axios";
import { useAuthStore } from "../../features/auth/model/authStore";

export const api = axios.create({
  baseURL: "/api",
  withCredentials: true, // ✅ REQUIRED FOR COOKIES
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const status = error.response?.status;

    if (status === 401 || status === 403) {
      useAuthStore.getState().logout();
    }

    return Promise.reject(error);
  }
);