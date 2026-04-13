import { api } from "../../../shared/api/axios";

export type RoleLog = {
    id: number;
    userId: number;
    userName: String;
    oldRole: String;
    newRole: String;
    changedBy: number;
    changedByName: String;
    createdAt: string;
};

export const getRoleLogs = async (page = 1, limit = 10) => {
    const res = await api.get("/role-logs", {
        params: { page, limit },
    });
    return res.data.data;
};

export const getRoleLogsByUser = async (userId: number) => {
    const res = await api.get(`/role-logs/users/${userId}`);
    return res.data.data;
};